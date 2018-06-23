package edu.itba.paw.jimi.persistence;

import edu.itba.paw.jimi.interfaces.daos.DishDao;
import edu.itba.paw.jimi.models.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Repository
public class DishJdbcDao implements DishDao {
	
	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert jdbcInsert;
	
	
	private final static RowMapper<Dish> ROW_MAPPER = new RowMapper<Dish>() {
		public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Dish(rs.getString("name"), rs.getFloat("price"), rs.getInt("dishid"), rs.getInt("stock"));
		}
	};

	//TODO: Hay que quitar?
	@Autowired
	public DishJdbcDao(final DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("dishes")
				.usingGeneratedKeyColumns("dishid");
	}
	
	
	public Dish findById(final long id) {
		final List<Dish> list = jdbcTemplate.query("SELECT * FROM dishes WHERE dishid = ?", ROW_MAPPER, id);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public Dish create(String name, float price, int stock) {
		final Map<String, Object> args = new HashMap<String, Object>();
		args.put("name", name);
		args.put("price", price);
		args.put("stock", stock);
		final Number userId = jdbcInsert.executeAndReturnKey(args);
		return new Dish(name, price, userId.longValue(), stock);
	}
	
	public int update(Dish dish) {
		return jdbcTemplate.update("UPDATE dishes SET (name, price, stock) = (?, ?, ?) WHERE dishid = ?", dish.getName(), dish.getPrice(), dish.getStock(), dish.getId());
		
	}
	
	public Collection<Dish> findAll() {
		return jdbcTemplate.query("SELECT * FROM dishes", ROW_MAPPER);
	}
}
