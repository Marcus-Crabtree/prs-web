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

@RestController
@RequestMapping("/line-items")
public class LineItemController {
	@Autowired
	private LineItemRepository lineItemRepo;
	@GetMapping ("/")
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
			jr = JsonResponse.getInstance(lineItem);
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
			lineItem = lineItemRepo.save(lineItem);
			jr = JsonResponse.getInstance(lineItem);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error updating Line Item: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

//delete method
	@DeleteMapping("/{id}")
	public JsonResponse deleteLineItem(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			lineItemRepo.deleteById(id);
			jr = JsonResponse.getInstance(id);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error deleting Line Item: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}
	//@GetMapping("/lines-for-pr/{id}")
    //public JsonResponse getLineItemsForRequest(@PathVariable int id) {
    //    JsonResponse jr = null;
       // Optional<Request> requests = requestRepo.findById(id);
      //  List<LineItem> lineItems = lineItemRepo.findAllByRequest(requests);

     //   if (!lineItems.isEmpty()) {
     //       jr = JsonResponse.getInstance(lineItems);
     //   } else {
      //      jr = JsonResponse.getErrorInstance("No lineItems found for request id: " + id);
     //   }
      //  return jr;
   // }


}
