package edu.itba.paw.jimi.interfaces.services;

import edu.itba.paw.jimi.models.Dish;
import edu.itba.paw.jimi.models.Order;
import edu.itba.paw.jimi.models.OrderStatus;
import edu.itba.paw.jimi.models.utils.QueryParams;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;


public interface OrderService {
	
	/**
	 * This creates a Order.
	 *
	 * @param status   The OrderStatus.
	 * @param openedAt The timestamp when this order became opened.
	 * @param closedAt The timestamp when this order became closed.
	 * @param diners   The diners of the order.
	 * @return created order
	 */
	Order create(OrderStatus status, Timestamp openedAt, Timestamp closedAt, int diners);
	
	/**
	 * Adds a dish to the order, if it is already there it increments the amount of said dish.
	 *
	 * @param order The order in which to operate.
	 * @param dish  The dish to add.
	 * @return The resulting amount of passed dish.
	 */
	int addDish(Order order, Dish dish);
	
	/**
	 * Adds n dishes to the order, if it is already there it increments the amount of said dish.
	 *
	 * @param order  The order in which to operate.
	 * @param dish   The dish to add.
	 * @param amount The amount to add.
	 * @return The resulting amount of passed dish.
	 */
	int addDishes(Order order, Dish dish, int amount);
	
	/**
	 * Removes a dish from the order, only one. If there was 2 of passed dish, 1 will remain.
	 * To remove all dishes of the same kind see removeAllUndoneDish.
	 *
	 * @param order The order in which to operate.
	 * @param dish  The dish to remove.
	 * @return The resulting amount of passed dish.
	 */
	int removeOneUndoneDish(Order order, Dish dish);

	/**
	 * Removes amount dishes from the order.
	 *
	 * @param order  The order in which to operate.
	 * @param dish   The dish to remove from.
	 * @param amount The amount to remove.
	 * @return The resulting amount of passed dish.
	 */
	int removeUndoneDish(Order order, Dish dish, int amount);
	
	/**
	 * Removes all instances of a dish from the order.
	 *
	 * @param order The order in which to operate.
	 * @param dish  The dish to remove completely.
	 * @return the amount of dishes left of this dish. (should be 0).
	 */
	int removeAllUndoneDish(Order order, Dish dish);
	
	/**
	 * Updates an undone dish amount in an order.
	 *
	 * @param order  The order in which to operate.
	 * @param dish   The dish to update.
	 * @param amount The new amount to set to the undone dish in the order.
	 */
	void setNewUndoneDishAmount(Order order, Dish dish, int amount);
	
	/**
	 * Find out of given order contains a given dishId.
	 *
	 * @param order  The order in which to operate.
	 * @param dishId The dish id to look for.
	 * @return true if order contains dish with dishId.
	 */
	boolean containsDish(Order order, int dishId);
	
	/**
	 * Get dish when a given order contains a given dishId.
	 *
	 * @param order  The order in which to operate.
	 * @param dishId The dish id to look for.
	 * @return true if order contains dish with dishId.
	 */
	Dish getDishById(Order order, int dishId);
	
	/**
	 * Sets the timestamp for openedAt and changes the status open.
	 *
	 * @param order the order to open.
	 */
	void open(Order order);
	
	/**
	 * Sets the timestamp for closedAt and changes the status to closed.
	 *
	 * @param order
	 */
	void close(Order order);
	
	/**
	 * Sets the timestamp for closedAt and changes the status to canceled.
	 *
	 * @param order
	 */
	void cancel(Order order);
	
	/**
	 * Sets the amount of dinners.
	 *
	 * @param order  The order to modify.
	 * @param diners The positive amount of diners.
	 * @return The amount of diners saved.
	 */
	int setDiners(Order order, int diners);
	
	/**
	 * Returns the order with id.
	 *
	 * @param id the id.
	 * @return
	 */
	Order findById(long id);
	
	/**
	 * Finds all closed orders.
	 *
	 * @return A collection of said orders.
	 */
	Collection<Order> findAll();
	
	
	/**
	 * Finds all closed orders.
	 * @deprecated
	 * @return A collection of said orders.
	 */
	Collection<Order> findAll(QueryParams qp);
	
	/**
	 * Finds all closed orders.
	 *
	 * @return A collection of said orders.
	 */
	Collection<Order> findAll(int maxResults, int offset);

	/**
	 * Finds all closed orders.
	 * @deprecated
	 * @return A collection of said orders.
	 */
	Collection<Order> findAllRelevant(QueryParams qp);

	/**
	 * Finds all closed orders.
	 *
	 * @return A collection of said orders.
	 */
	Collection<Order> findAllRelevant(int maxResults, int offset);

	/**
	 * Finds all closed orders' total by month.
	 *
	 * @return A collection of said orders.
	 */
	Map getMonthlyOrderTotal();
	
	/**
	 * Finds all cancelled orders' total by month.
	 *
	 * @return A collection of said orders.
	 */
	Map getMonthlyOrderCancelled();
	
	/**
	 * Sets dish from order as done.
	 */
	void setDishAsDone(Order order, Dish dish);
	
	/**
	 * @return count of cancelled or closed orders.
	 */
	int getTotalRelevantOrders();
	
	/**
	 * Finds all open orders.
	 * @deprecated
	 * @return A collection of said orders in ascending order by open timestamp.
	 */
	Collection<Order> getActiveOrders(QueryParams qp);

	/**
	 * Finds all open orders.
	 *
	 * @return A collection of said orders in ascending order by open timestamp.
	 */
	Collection<Order> getActiveOrders(int maxResults, int offset);

	/**
	 * @return count of open orders.
	 */
	int getTotalActiveOrders();
	
	/**
	 * Finds all orders from the last given amount of minutes.
	 *
	 * @return A collection of said orders.
	 */
	Collection<Order> getOrdersFromLastMinutes(int minutes);
	
	/**
	 * Finds all undone dishes from all active orders.
	 *
	 * @return A collection of said dishes.
	 */
	Map getAllUndoneDishesFromAllActiveOrders();

	Map getAllUndoneDishesFromAllActiveOrders(QueryParams qp);
}
