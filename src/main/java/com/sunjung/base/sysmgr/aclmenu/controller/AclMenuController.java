package com.sunjung.base.sysmgr.aclmenu.controller;

import com.sunjung.base.sysmgr.aclmenu.domain.entity.AclMenu;
import com.sunjung.core.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 为 on 2017-4-8.
 */
@RestController
@RequestMapping(value = {"/base/sysmgr/aclmenu"})
public class AclMenuController extends BaseController<AclMenu> {

}
