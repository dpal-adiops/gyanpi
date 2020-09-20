package com.adiops.apigateway.learning.track.resource;

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

import com.adiops.apigateway.learning.track.service.LearningTrackService;
import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The resource class for LearningTrack 
 * @author Deepak Pal
 *
 */
@RestController
public class LearningTrackResource {

	@Autowired
	LearningTrackService mLearningTrackService;

	
	
	/**
	 * This method fetch a list of LearningTrack
	 * @return
	 */
	@ApiOperation(value = "Get a list of LearningTrack")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.OK, message = "learning_track data fetched.", response = LearningTrackRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
	
	@RequestMapping(value = "/api/learning_tracks", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public List<LearningTrackRO> getLearningTrackROs() {
		return mLearningTrackService.getLearningTrackROs();
	}
	
	/**
	 * This method upload a file
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "upload a file")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "File has been upload sucessfully.", response = LearningTrackRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
	@PostMapping("/api/learning_track/import")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				mLearningTrackService.importCSV(file);
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
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "Get by ID.", response = LearningTrackRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
 	@GetMapping("/api/learning_track/{id}")
    public ResponseEntity<LearningTrackRO> getLearningTrackById(@PathVariable("id") Long id) 
                                                    throws RestException {
        LearningTrackRO entity = mLearningTrackService.getLearningTrackById(id);
 
        return new ResponseEntity<LearningTrackRO>(entity, new HttpHeaders(), HttpStatus.OK);
    }
 
 	/**
	 * This method to get by ID
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "create or update")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "create or update.", response = LearningTrackRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
    @PostMapping("/api/learning_track")
    public ResponseEntity<LearningTrackRO> createOrUpdateLearningTrack(LearningTrackRO learning_track)
                                                    throws RestException {
        LearningTrackRO updated = mLearningTrackService.createOrUpdateLearningTrack(learning_track);
        return new ResponseEntity<LearningTrackRO>(updated, new HttpHeaders(), HttpStatus.OK);
    }
    
 	/**
	 * This method delete by  ID
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "delete by  ID")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "delete by  ID.", response = LearningTrackRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
    @DeleteMapping("/api/learning_track/{id}")
    public HttpStatus deleteLearningTrackById(@PathVariable("id") Long id) throws RestException {
        mLearningTrackService.deleteLearningTrackById(id);
        return HttpStatus.FORBIDDEN;
    }
	
}
