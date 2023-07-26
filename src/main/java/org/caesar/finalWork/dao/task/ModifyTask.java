package org.caesar.finalWork.dao.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@Data
@NoArgsConstructor
public class ModifyTask extends Task{

    public ModifyTask(String sql, Object ...params){
        this.sql = sql;
        this.params = new ArrayList<>();
        this.params.addAll(Arrays.asList(params));
    }

    @Override
    protected Object run(PreparedStatement ps) throws SQLException {
        return ps.executeUpdate();
    }

}
