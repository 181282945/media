package com.sunjung.core.security.resource.controller;

import com.sunjung.core.security.resource.annotation.Resc;
import com.sunjung.core.security.resource.entity.Resource;
import com.sunjung.core.security.resource.entity.ResourceType;
import com.sunjung.core.util.SpringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 为 on 2017-4-7.
 */
@RestController
@RequestMapping("/resource")
@Resc(name = "resource", descn = "资源权限", resourceType = ResourceType.MODULE)
public class ResourceController {

}
