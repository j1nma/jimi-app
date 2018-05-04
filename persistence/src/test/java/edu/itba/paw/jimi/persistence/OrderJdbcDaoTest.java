package edu.itba.paw.jimi.persistence;

import edu.itba.paw.jimi.interfaces.daos.DishDao;
import edu.itba.paw.jimi.interfaces.daos.OrderDao;
import edu.itba.paw.jimi.models.Dish;
import edu.itba.paw.jimi.models.Order;
import edu.itba.paw.jimi.models.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class OrderJdbcDaoTest {

    @Autowired
    private DataSource ds;

    private OrderDao orderDao;
    private DishDao dishDao;

    private JdbcTemplate jdbcTemplate;

    private static final String ORDER_TABLE_NAME = "orders";
    private static final String ORDER_ITEM_TABLE_NAME = "orders_items";

    private static final String DISH_NAME = "Cambuchá";
    private static final Float DISH_PRICE = 5.25F;
    private static final int DISH_STOCK = 5;

    private static final String DISH_NAME2 = "Hamburguesa";
    private static final Float DISH_PRICE2 = 92.6F;
    private static final int DISH_STOCK2 = 19;

    private static final String DISH_NAME3 = "Milanesa";
    private static final Float DISH_PRICE3 = 0.6F;
    private static final int DISH_STOCK3 = 1;

    private static final Timestamp OPENEDAT = new Timestamp(1525467178);
    private static final Timestamp CLOSEDAT = new Timestamp(1525467178 + 60*60);


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        orderDao = new OrderJdbcDao(ds);
        dishDao = new DishJdbcDao(ds);
        cleanDB();
    }

    private void cleanDB(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ORDER_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ORDER_ITEM_TABLE_NAME);
    }

    @Test
    public void testCreate() {
        orderDao.create(OrderStatus.INACTIVE, null, null);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, ORDER_TABLE_NAME));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, ORDER_ITEM_TABLE_NAME));

        cleanDB();
    }

    @Test
    public void testFindByIdEmpty() {
        final Order order = orderDao.create(OrderStatus.INACTIVE, null, null);
        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);
        cleanDB();
    }

    @Test
    public void testFindByIdWithValues() {
        final Order order = orderDao.create(OrderStatus.OPEN, OPENEDAT, CLOSEDAT);
        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);
        assertEquals(OrderStatus.OPEN.getId(), dbOrder.getStatus().getId());
        assertEquals(OPENEDAT, dbOrder.getOpenedAt());
        assertEquals(CLOSEDAT, dbOrder.getClosedAt());
        cleanDB();
    }

    @Test
    public void testFindByIdOneDish() {
        final Order order = orderDao.create(OrderStatus.INACTIVE, null, null);
        final Dish dish = dishDao.create(DISH_NAME, DISH_PRICE, DISH_STOCK);
        order.setDish(dish, 1);

        orderDao.update(order);

        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNotNull(dbOrder.getDishes().get(dish));


        int amount = dbOrder.getDishes().get(dish);
        assertEquals(1, amount);

        Dish dbDish = dbOrder.getDishes().keySet().iterator().next();
        assertEquals(dish.getName(), dbDish.getName());
        assertEquals(dish.getPrice(), dbDish.getPrice());
        assertEquals(dish.getStock(), dbDish.getStock());
        assertEquals(dish.getId(), dbDish.getId());

        cleanDB();
    }

    @Test
    public void testFindByIdOneDishThrice() {
        final Order order = orderDao.create(OrderStatus.INACTIVE, null, null);
        final Dish dish = dishDao.create(DISH_NAME, DISH_PRICE, DISH_STOCK);
        order.setDish(dish, 3);

        orderDao.update(order);

        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNotNull(dbOrder.getDishes().get(dish));


        int amount = dbOrder.getDishes().get(dish);
        assertEquals(3, amount);

        Dish dbDish = dbOrder.getDishes().keySet().iterator().next();
        assertEquals(dish.getName(), dbDish.getName());
        assertEquals(dish.getPrice(), dbDish.getPrice());
        assertEquals(dish.getStock(), dbDish.getStock());
        assertEquals(dish.getId(), dbDish.getId());

        cleanDB();
    }

    @Test
    public void testFindByIdSeveralDishes() {
        final Order order = orderDao.create(OrderStatus.INACTIVE, null, null);

        final Dish dish = dishDao.create(DISH_NAME, DISH_PRICE, DISH_STOCK);
        order.setDish(dish, 3);

        final Dish dish2 = dishDao.create(DISH_NAME2, DISH_PRICE2, DISH_STOCK2);
        order.setDish(dish2, 5);

        final Dish dish3 = dishDao.create(DISH_NAME3, DISH_PRICE3, DISH_STOCK3);
        order.setDish(dish3, 1);


        orderDao.update(order);


        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNotNull(dbOrder.getDishes().get(dish));
        assertNotNull(dbOrder.getDishes().get(dish2));
        assertNotNull(dbOrder.getDishes().get(dish3));


        int amount = dbOrder.getDishes().get(dish);
        assertEquals(3, amount);

        int amount2 = dbOrder.getDishes().get(dish2);
        assertEquals(5, amount2);

        int amount3 = dbOrder.getDishes().get(dish3);
        assertEquals(1, amount3);


        for (Dish dbDish : dbOrder.getDishes().keySet()) {
            if (dbDish.getId() == dish.getId()) {
                assertEquals(dish.getName(), dbDish.getName());
                assertEquals(dish.getPrice(), dbDish.getPrice());
                assertEquals(dish.getStock(), dbDish.getStock());
                assertEquals(dish.getId(), dbDish.getId());
            } else if (dbDish.getId() == dish2.getId()) {
                assertEquals(dish2.getName(), dbDish.getName());
                assertEquals(dish2.getPrice(), dbDish.getPrice());
                assertEquals(dish2.getStock(), dbDish.getStock());
                assertEquals(dish2.getId(), dbDish.getId());
            } else if (dbDish.getId() == dish3.getId()) {
                assertEquals(dish3.getName(), dbDish.getName());
                assertEquals(dish3.getPrice(), dbDish.getPrice());
                assertEquals(dish3.getStock(), dbDish.getStock());
                assertEquals(dish3.getId(), dbDish.getId());
            } else {
                assertEquals(0, 1);
            }

        }

        cleanDB();
    }
    @Test
    public void testFindByIdAddAndRemove() {
        final Order order = orderDao.create(OrderStatus.INACTIVE, null, null);
        final Dish dish = dishDao.create(DISH_NAME, DISH_PRICE, DISH_STOCK);
        order.setDish(dish, 1);


        orderDao.update(order);

        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNotNull(dbOrder.getDishes().get(dish));


        int amount = dbOrder.getDishes().get(dish);
        assertEquals(1, amount);

        Dish dbDish = dbOrder.getDishes().keySet().iterator().next();
        assertEquals(dish.getName(), dbDish.getName());
        assertEquals(dish.getPrice(), dbDish.getPrice());
        assertEquals(dish.getStock(), dbDish.getStock());
        assertEquals(dish.getId(), dbDish.getId());

        order.setDish(dish, 0);

        orderDao.update(order);


        dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNull(dbOrder.getDishes().get(dish));

        cleanDB();
    }
    @Test
    public void testFindByIdAddAndRemoveButNoDelete() {
        final Order order = orderDao.create(OrderStatus.INACTIVE, null, null);
        final Dish dish = dishDao.create(DISH_NAME, DISH_PRICE, DISH_STOCK);
        order.setDish(dish, 2);


        orderDao.update(order);

        Order dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNotNull(dbOrder.getDishes().get(dish));


        int amount = dbOrder.getDishes().get(dish);
        assertEquals(2, amount);

        Dish dbDish = dbOrder.getDishes().keySet().iterator().next();
        assertEquals(dish.getName(), dbDish.getName());
        assertEquals(dish.getPrice(), dbDish.getPrice());
        assertEquals(dish.getStock(), dbDish.getStock());
        assertEquals(dish.getId(), dbDish.getId());

        order.setDish(dish, 1);

        orderDao.update(order);


        dbOrder = orderDao.findById(order.getId());
        assertNotNull(dbOrder);

        assertNotNull(dbOrder.getDishes().get(dish));


        amount = dbOrder.getDishes().get(dish);
        assertEquals(1, amount);

        dbDish = dbOrder.getDishes().keySet().iterator().next();
        assertEquals(dish.getName(), dbDish.getName());
        assertEquals(dish.getPrice(), dbDish.getPrice());
        assertEquals(dish.getStock(), dbDish.getStock());
        assertEquals(dish.getId(), dbDish.getId());

        cleanDB();
    }

}
