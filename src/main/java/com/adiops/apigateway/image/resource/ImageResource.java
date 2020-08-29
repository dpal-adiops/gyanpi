package com.adiops.apigateway.image.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adiops.apigateway.common.helper.CSVHelper;
import com.adiops.apigateway.common.response.ResponseMessage;
import com.adiops.apigateway.common.response.ResponseStatusConstants;
import com.adiops.apigateway.common.response.RestException;

import com.adiops.apigateway.image.service.ImageService;
import com.adiops.apigateway.image.resourceobject.ImageRO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The resource class for Image 
 * @author Deepak Pal
 *
 */
@RestController
public class ImageResource {

	@Autowired
	ImageService mImageService;

	
	
	/**
	 * This method fetch a list of Image
	 * @return
	 */
	@ApiOperation(value = "Get a list of Image")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.OK, message = "image data fetched.", response = ImageRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
	
	@RequestMapping(value = "/api/images", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public List<ImageRO> getImageROs() {
		return mImageService.getImageROs();
	}
	
	/**
	 * This method upload a file
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "upload a file")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "File has been upload sucessfully.", response = ImageRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
	@PostMapping("/api/image/import")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				mImageService.importCSV(file);
				String message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (RestException e) {
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Please upload a csv file!"));
	}

	/**
	 * This method to get by ID
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "get by ID")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "Get by ID.", response = ImageRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
 	@GetMapping("/api/image/{id}")
    public ResponseEntity<ImageRO> getImageById(@PathVariable("id") Long id) 
                                                    throws RestException {
        ImageRO entity = mImageService.getImageById(id);
 
        return new ResponseEntity<ImageRO>(entity, new HttpHeaders(), HttpStatus.OK);
    }
 
 	/**
	 * This method to get by ID
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "create or update")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "create or update.", response = ImageRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
    @PostMapping("/api/image")
    public ResponseEntity<ImageRO> createOrUpdateImage(ImageRO image)
                                                    throws RestException {
        ImageRO updated = mImageService.createOrUpdateImage(image);
        return new ResponseEntity<ImageRO>(updated, new HttpHeaders(), HttpStatus.OK);
    }
    
 	/**
	 * This method delete by  ID
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "delete by  ID")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "delete by  ID.", response = ImageRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
    @DeleteMapping("/api/image/{id}")
    public HttpStatus deleteImageById(@PathVariable("id") Long id) throws RestException {
        mImageService.deleteImageById(id);
        return HttpStatus.FORBIDDEN;
    }
	
}
