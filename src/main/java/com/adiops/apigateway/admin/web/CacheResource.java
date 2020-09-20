package com.adiops.apigateway.admin.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.adiops.apigateway.account.web.bo.view.CacheMgr;
import com.adiops.apigateway.app.role.resourceobject.AppRoleRO;
import com.adiops.apigateway.common.response.ResponseStatusConstants;
import com.adiops.apigateway.common.response.RestException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CacheResource {

	
	/**
	 * This method delete by  ID
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "delete by  ID")
	@ApiResponses({
			@ApiResponse(code = ResponseStatusConstants.CREATED, message = "delete by  ID.", response = AppRoleRO.class),
			@ApiResponse(code = ResponseStatusConstants.INTERNAL_SERVER_ERROR, message = ResponseStatusConstants.INTERNAL_SERVER_ERROR_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.BAD_REQUEST, message = ResponseStatusConstants.BAD_REQUEST_MESSAGE, response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.NOT_FOUND, message = ResponseStatusConstants.NOT_FOUND_MESSAGE, response = RestException.class),
			@ApiResponse(code = 412, message = "Precondition Failed", response = RestException.class),
			@ApiResponse(code = ResponseStatusConstants.FORBIDDEN, message = ResponseStatusConstants.FORBIDDEN_MESSAGE, response = RestException.class) })
    @DeleteMapping("/api/cache/{key}")
    public HttpStatus deleteCacheByKey(@PathVariable("key") String key) throws RestException {
		if("all".equalsIgnoreCase(key))
			CacheMgr.clear();
		CacheMgr.clearByKey(key);
        return HttpStatus.FORBIDDEN;
    }
	
	
}
