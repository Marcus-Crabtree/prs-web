package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineItemRepository;
import com.prs.db.RequestRepository;

@CrossOrigin()
@RestController
@RequestMapping("/line-items")
public class LineItemController {
	@Autowired
	private LineItemRepository lineItemRepo;
	@Autowired
	private RequestRepository requestRepo;

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<LineItem> lineItems = lineItemRepo.findAll();
		if (lineItems.size() > 0) {
			jr = JsonResponse.getInstance(lineItems);
		} else {
			jr = JsonResponse.getErrorInstance("No Line Items found.");
		}
		return jr;
	}

// get method
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<LineItem> lineItem = lineItemRepo.findById(id);
		if (lineItem.isPresent()) {
			jr = JsonResponse.getInstance(lineItem.get());
		} else {
			jr = JsonResponse.getErrorInstance("No Line Item found for ID: " + id);
		}

		return jr;
	}

// 'create' method
	@PostMapping("/")
	public JsonResponse createLineItem(@RequestBody LineItem lineItem) {
		JsonResponse jr = null;

		try {
			lineItem = lineItemRepo.save(lineItem);
			jr = JsonResponse.getInstance(lineItemRepo.save(lineItem));
			recalcTotal(lineItem.getRequest());
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error creating Line Item: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

// update method
	@PutMapping("/")
	public JsonResponse updateLineItem(@RequestBody LineItem lineItem) {
		JsonResponse jr = null;

		try {
			if (lineItemRepo.existsById(lineItem.getId())) {
				jr = JsonResponse.getInstance(lineItemRepo.save(lineItem));
				recalcTotal(lineItem.getRequest());
			} else {
				jr = JsonResponse
						.getErrorInstance("Error updating Line Item. ID: " + lineItem.getId() + " doesn't exist.");
			}
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

//delete method
	@DeleteMapping("/{id}")
	public JsonResponse deleteLineItem(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			if (lineItemRepo.existsById(id)) {
				Request request = lineItemRepo.findById(id).get().getRequest();
				lineItemRepo.deleteById(id);
				recalcTotal(request);
				jr = JsonResponse.getInstance("Delete Successful!");
			} else {
				jr = JsonResponse.getInstance("Error deleting Line Item. id: " + id + " doesn't exist.");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	@GetMapping("/lines-for-pr/{id}")
	public JsonResponse getLineItemById(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findByRequestId(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	void recalcTotal(Request request) {
		double lineItemtotal = 0;
		List<LineItem> lineItems = lineItemRepo.findByRequestId(request.getId());
		for (LineItem line : lineItems) {
			lineItemtotal += line.getLineTotal();
		}
		request.setTotal(lineItemtotal);
		try {
			requestRepo.save(request);
		} catch (Exception e) {
			throw e;
		}
	}

}
