package edu.itba.paw.jimi.services;

import edu.itba.paw.jimi.interfaces.services.DishService;
import edu.itba.paw.jimi.interfaces.services.OrderService;
import edu.itba.paw.jimi.interfaces.services.StatsService;
import edu.itba.paw.jimi.interfaces.services.TableService;
import edu.itba.paw.jimi.models.Dish;
import edu.itba.paw.jimi.models.Table;
import edu.itba.paw.jimi.models.TableStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {
	
	private final int infBound = 50;
	
	@Autowired
	private DishService dishService;
	
	@Autowired
	private TableService tableService;
	
	@Autowired
	private OrderService orderService;
	
	
	public int getBusyTablesUnits() {
		int busy = 0;
		for (Table t : tableService.findAll()) {
			if (t.getStatus() == TableStatus.BUSY)
				busy += 1;
		}
		return busy;
	}
	
	public int getBusyTables() {
		return (int) ((getBusyTablesUnits() * 100.0) / tableService.findAll().size());
	}
	
	public int getFreeTablesUnits() {
		int free = 0;
		for (Table t : tableService.findAll()) {
			if (t.getStatus() == TableStatus.FREE)
				free += 1;
		}
		return free;
	}
	
	public int getFreeTables() {
		return (int) ((getFreeTablesUnits() * 100.0) / tableService.findAll().size());
	}
	
	public int getStockState() {
		int underBound = 0;
		for (Dish d : dishService.findAll()) {
			if (d.getStock() <= infBound)
				underBound += 1;
		}
		return (int) ((underBound * 100.0) / dishService.findAll().size());
	}
	
	public int getDinersToday() {
		//TODO
		return 0;
	}
	
	public int getDishesSold() {
		//TODO
		return 0;
	}
	
}