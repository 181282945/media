package com.aisino.e9.controller.sysmgr.taxclassification;

import com.aisino.core.controller.BaseController;
import com.aisino.e9.entity.taxclassification.pojo.TaxClassification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by 为 on 2017-6-19.
 */
@RestController
@RequestMapping(path = TaxClassificationController.PATH)
public class TaxClassificationController extends BaseController {
    final static String PATH = "/sysmgr/taxclassification";

}
