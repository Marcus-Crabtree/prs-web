package com.prs.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.Request;
import com.prs.db.RequestRepository;

@CrossOrigin()
@RestController
@RequestMapping("/requests")
public class RequestController {
	@Autowired
	private RequestRepository requestRepo;

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<Request> requests = requestRepo.findAll();
		if (requests.size() > 0) {
			jr = JsonResponse.getInstance(requests);
		} else {
			jr = JsonResponse.getErrorInstance("No requests found.");
		}
		return jr;
	}

// get method
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Request> request = requestRepo.findById(id);
		if (request.isPresent()) {
			jr = JsonResponse.getInstance(request.get());
		} else {
			jr = JsonResponse.getErrorInstance("No request found for ID: " + id);
		}

		return jr;
	}

// 'create' method
	@PostMapping("/")
	public JsonResponse createRequest(@RequestBody Request request) {
		JsonResponse jr = null;
		request.setStatus("New");
		request.setSubmittedDate(LocalDateTime.now());
		try {
			request = requestRepo.save(request);
			jr = JsonResponse.getInstance(request);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error creating request: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

// update method
	@PutMapping("/")
	public JsonResponse updateRequest(@RequestBody Request request) {
		JsonResponse jr = null;

		try {
			request = requestRepo.save(request);
			jr = JsonResponse.getInstance(request);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error updating request: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

//delete method
	@DeleteMapping("/{id}")
	public JsonResponse deleteRequest(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			requestRepo.deleteById(id);
			jr = JsonResponse.getInstance(id);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error deleting request: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

//submit-review
	@PutMapping("/submit-review")
	public JsonResponse submitForReview(@RequestBody Request request) {
		JsonResponse jr = null;

		try {
			if (request.getTotal() <= 50) {
				request.setStatus("Approved");
			} else {
				request.setStatus("Review");
			}

			request.setSubmittedDate(LocalDateTime.now());
			request = requestRepo.save(request);
			jr = JsonResponse.getInstance(request);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error Submitting for review" + e.getMessage());

		}
		return jr;
	}

	// approve method
	@PutMapping("/approve")
	public JsonResponse submitApproval(@RequestBody Request request) {
		JsonResponse jr = null;
		try {
			request.setStatus("Approved");
			request.setSubmittedDate(LocalDateTime.now());
			request = requestRepo.save(request);
			jr = JsonResponse.getInstance(request);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error processing approval" + e.getMessage());

		}
		return jr;
	}

	// reject method
	@PutMapping("/reject")
	public JsonResponse submitRejection(@RequestBody Request request) {
		JsonResponse jr = null;
		try {
			request.setStatus("Rejected");
			request.setSubmittedDate(LocalDateTime.now());
			request = requestRepo.save(request);
			jr = JsonResponse.getInstance(request);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error processing rejection" + e.getMessage());

		}
		return jr;
	}

	// request review
	@GetMapping("/list-review/{id}")
	public JsonResponse listRequestsForReview(@PathVariable int id) {
        JsonResponse jr = null;
        List<Request> requests = requestRepo.findAllByStatusAndUserIdNot("Review", id);
            if (requests.size() > 0) {
                jr = JsonResponse.getInstance(requests);
            } else {
                jr = JsonResponse.getErrorInstance("No requests found for ID: "+id);
            }
        return jr;
    }

}
