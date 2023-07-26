package org.caesar.finalWork.dao;



import org.caesar.finalWork.dao.task.ModifyTask;
import org.caesar.finalWork.dao.task.ObjectInfo;
import org.caesar.finalWork.dao.task.QueryTask;
import org.caesar.finalWork.dao.task.Task;
import org.caesar.finalWork.util.ThreadUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Thread.sleep;

public class ConnectionPool{

    private static final ConnectionPool singleton = new ConnectionPool();
    private static final int MIN_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 50;
    private static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://localhost:3306/sale_sys";
    private static String USERNAME = "root";
    private static String PASSWORD = "1234";
    private Integer connectionInUse = 0;
    private Map<Class, ObjectInfo> infos = new HashMap<>();
    //用于同步wait和notify
    private Object lock = new Object();
    //共享连接资源
    private List<Connection> connections = new LinkedList<>();

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ConnectionPool()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkConnections();
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static ConnectionPool getInstance(){
        return singleton;
    }

    //执行静态sql
    private Object execute(Task task) throws Exception {

        prepareTask(task);
        return task.execute();
    }

    //查询单个对象
    public Object selectOne(Task task) throws Exception {

        if(!(task instanceof QueryTask))
            throw new IllegalArgumentException("非法任务类型：任务不是查询任务");

        Object result = execute(task);

        if(!(result instanceof List<?>))
            throw new IllegalArgumentException("非法任务类型：任务返回结果不是List");

        List<Object> objects = (List<Object>) result;

        if(objects.size() == 0)
            return null;

        return objects.get(0);
    }

    //查询多个对象
    public Object selectAll(Task task) throws Exception {

        if(!(task instanceof QueryTask))
            throw new IllegalArgumentException("非法任务类型：任务不是查询任务");

        Object result = execute(task);

        if(!(result instanceof List<?>))
            throw new IllegalArgumentException("非法任务类型：任务返回结果不是List");

        //直接返回结果，以便mapper强转成List<T>
        return result;
    }

    public Object selectObject(Task task) throws Exception {
        return execute(task);
    }

    //动态查询
    public Object dynamicQuery(Task task) throws Exception {

        prepareTask(task);

        return task.executeDynamic();
    }

    //增删改
    public boolean modify(Task task){

        if(!(task instanceof ModifyTask)){
            System.out.println("非法任务类型：任务不是修改任务");
            return false;
        }

        Object result = false;

        try {
            result = execute(task);
        } catch (Exception e) {
            return false;
        }

        if(!(result instanceof Integer)){
            System.out.println("非法任务类型：任务返回结果不是int");
            return false;
        }

        return (int) result > 0;
    }

    private void prepareTask(Task task) throws Exception{
        Connection connection = getConnection();

        //若为查询任务且未缓存反射信息，则创建ObjectInfo
        if(task instanceof QueryTask && infos.get(task.getClazz()) == null){
            infos.put(task.getClazz(), new ObjectInfo(task.getClazz()));
        }

        task.setInfo(infos.get(task.getClazz()));
        task.setConnection(connection);
        task.setConnectionPool(this);
    }

    private Connection getConnection() throws InterruptedException, SQLException {

        Connection connection = null;
        //若连接池中有连接，则直接返回
        synchronized (connections){
            if(connections.size() > 0)
                return connections.remove(connections.size() - 1);
        }

        //若连接池中无连接但未被占满，则创建连接
        synchronized (connectionInUse) {
            if (connectionInUse < MAX_POOL_SIZE) {
                connectionInUse++;
                return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            }
        }

        //否则等待连接被释放
        synchronized (lock){
            //若等待超过10s，则视为获取连接超时
            lock.wait(10000);

            //无连接可用
            if(connections.size() == 0){
                new RuntimeException("获取连接超时");
            }

            //获取释放的连接
            connectionInUse++;
            connection = connections.remove(connections.size() - 1);
        }

        return connection;
    }

    //Observer pattern(Observer)
    public void updateState(Connection connection) throws SQLException{

        synchronized (lock){
            //把连接放回连接池
            connectionInUse --;
            this.connections.add(connection);
            /*System.out.println("当前连接数：" + connectionInUse);*/
            lock.notify();
        }

    }

    private void checkConnections() throws InterruptedException, SQLException {

        while (true){

            sleep(10000);

            if(ThreadUtil.getCountOfWaitingThreads(lock) == 0)
                while(connections.size() > MIN_POOL_SIZE){
                    //释放多余连接
                    connections.remove(connections.size() - 1).close();
                }
        }
    }

}
