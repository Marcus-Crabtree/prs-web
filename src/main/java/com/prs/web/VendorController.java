package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.JsonResponse;
import com.prs.business.Vendor;
import com.prs.business.Vendor;
import com.prs.db.VendorRepository;

@RestController
@RequestMapping("/vendors")
public class VendorController {
	@Autowired
	private VendorRepository vendorRepo;
	@GetMapping ("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<Vendor> vendors = vendorRepo.findAll();
		if (vendors.size() > 0) {
			jr = JsonResponse.getInstance(vendors);
		} else {
			jr = JsonResponse.getErrorInstance("No vendors found.");
		}
		return jr;
	}

// get method
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<Vendor> vendor = vendorRepo.findById(id);
		if (vendor.isPresent()) {
			jr = JsonResponse.getInstance(vendor.get());
		} else {
			jr = JsonResponse.getErrorInstance("No vendor found for ID: " + id);
		}

		return jr;
	}
	// 'create' method
		@PostMapping("/")
		public JsonResponse createVendor(@RequestBody Vendor vendor) {
			JsonResponse jr = null;

			try {
				vendor = vendorRepo.save(vendor);
				jr = JsonResponse.getInstance(vendor);
			} catch (DataIntegrityViolationException dive) {
				jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
				dive.printStackTrace();
			} catch (Exception e) {
				jr = JsonResponse.getErrorInstance("Error creating vendor: " + e.getMessage());
				e.printStackTrace();
			}

			return jr;
		}

	// update method
		@PutMapping("/")
		public JsonResponse updateVendor(@RequestBody Vendor vendor) {
			JsonResponse jr = null;

			try {
				vendor = vendorRepo.save(vendor);
				jr = JsonResponse.getInstance(vendor);
			} catch (Exception e) {
				jr = JsonResponse.getErrorInstance("Error updating vendor: " + e.getMessage());
				e.printStackTrace();
			}

			return jr;
		}

	//delete method
		@DeleteMapping("/{id}")
		public JsonResponse deleteVendor(@PathVariable int id) {
			JsonResponse jr = null;

			try {
				vendorRepo.deleteById(id);
				jr = JsonResponse.getInstance(id);
			} catch (Exception e) {
				jr = JsonResponse.getErrorInstance("Error deleting vendor: " + e.getMessage());
				e.printStackTrace();
			}

			return jr;
		}





}
