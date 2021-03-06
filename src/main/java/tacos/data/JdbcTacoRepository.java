package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.domain.Ingredient;
import tacos.domain.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for(Ingredient ingredient : taco.getIngredients()){
            saveIngredientToTaco(ingredient, tacoId);
        }


        return taco;
    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient) " +
                "values (?, ?)",
                tacoId, ingredient.getId());
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAT(new Date());
        PreparedStatementCreatorFactory preparedStatementCreatorFactory =
                new PreparedStatementCreatorFactory(
                        "insert into Taco (name, createdAt) values(?,?)",
                        Types.VARCHAR, Types.TIMESTAMP);

        preparedStatementCreatorFactory.setReturnGeneratedKeys(true); // default is null

        PreparedStatementCreator psc =
                preparedStatementCreatorFactory
                        .newPreparedStatementCreator(
                                Arrays.asList( // supply values as list
                                        taco.getName(),
                                        new Timestamp(taco.getCreatedAT().getTime())
                                ));

        KeyHolder keyHolder = new GeneratedKeyHolder(); //
        jdbc.update(psc, keyHolder);

        return keyHolder.getKey().longValue(); // return null unlsess setReturnGeneratedKeys is set
    }                                           // to true on preparedStatementCreatorFactory
}
