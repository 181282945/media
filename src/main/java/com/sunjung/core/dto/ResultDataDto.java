package com.sunjung.core.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.Gson;
import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.exception.RuntimeFunctionException;
import com.sunjung.core.exception.RuntimeOtherException;
import com.sunjung.core.exception.RuntimeServiceException;
import com.sunjung.core.exception.RuntimeWebException;
import com.sunjung.core.mybatis.specification.PageAndSort;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.validation.BindException;

public class ResultDataDto {

	/**
	 * 200-成功
	 */
	public final static String CODE_SUCCESS = "200";

	/**
	 * 500-业务逻辑错误
	 */
	public final static String CODE_ERROR_SERVICE = "500";

	/**
	 * 501-功能不完善，无对应方法
	 */
	public final static String CODE_ERROR_FUNCTION = "501";

	/**
	 * 502-网络异常
	 */
	public final static String CODE_ERROR_WEB = "502";

	/**
	 * 503-未知其它
	 */
	public final static String CODE_ERROR_OTHER = "503";

	/**
	 * S-操作成功
	 */
	public final static String CODE_OPERATE_SUCCESS = "S";

	/**
	 * S-操作成功
	 */
	public final static String CODE_OPERATE_FAILURE = "F";


	/**
	 * 文件流导入返回值，因IE不支持json返回
	 * @return
	 */
	public static void addImportSuccess(HttpServletResponse response, String message) {
		try {
			response.setContentType("text/html");
			response.getWriter().write(new Gson().toJson(ResultDataDto.addSuccess(message)));
		} catch (IOException e) {
			throw new RuntimeServiceException(e);
		}
	}
	/**
	 * 文件流导入返回值，因IE不支持json返回
	 * @return
	 */
	public static void addImportSuccess(HttpServletResponse response) {
		try {
			response.setContentType("text/html");
			response.getWriter().write(new Gson().toJson(ResultDataDto.addSuccess("上传成功")));
		} catch (IOException e) {
			throw new RuntimeServiceException(e);
		}
	}

	public static void addImportError(HttpServletResponse response, String message) {
		try {
			response.setContentType("text/html");
			response.getWriter().write(new Gson().toJson(new ResultDataDto(CODE_ERROR_SERVICE,CODE_OPERATE_FAILURE, message)));
		} catch (IOException e) {
			throw new RuntimeServiceException(e);
		}
	}

	public static ResultDataDto addSuccess() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS, null);
	}

	public static ResultDataDto addSuccess(String message) {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS, message);
	}

	public static ResultDataDto addAddSuccess() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS, "新增成功");
	}

	public static ResultDataDto addUpdateSuccess() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS, "更新成功");
	}

	public static ResultDataDto addUpdateCheckSuccess() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS, "确定成功");
	}

	public static ResultDataDto addDeleteSuccess() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS, "删除成功");
	}

	public static ResultDataDto addOperationSuccess() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_SUCCESS,"操作成功");
	}

	public static ResultDataDto addOperationFailure() {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_FAILURE,"操作失败");
	}

	public static ResultDataDto addOperationFailure(String message) {
		return new ResultDataDto(CODE_SUCCESS,CODE_OPERATE_FAILURE,message);
	}

	public ResultDataDto() {
		super();
	}

	public ResultDataDto(String code,String operateCode,String message) {
		super();
		this.code = code;
		this.message = message;
		this.operateCode = operateCode;
	}

	public ResultDataDto(String message) {
		super();
		this.code = CODE_SUCCESS;
		this.operateCode = CODE_OPERATE_SUCCESS;
		this.message = message;
	}

	/**
	 * 返回单个实体
	 */
	public<T extends BaseEntity> ResultDataDto(T entity) {
		super();
		this.code = CODE_SUCCESS;
		this.operateCode = CODE_OPERATE_SUCCESS;
		this.datas = entity;
	}

	/**
	 * 返回集合类型
	 */
	public ResultDataDto(List<?> list) {
		super();
		this.code = CODE_SUCCESS;
		this.operateCode = CODE_OPERATE_SUCCESS;
		this.datas = list;
	}

	/**
	 * 返回Map集合类型
	 */
	public ResultDataDto(Map<String, Object> map) {
		super();
		this.code = CODE_SUCCESS;
		this.operateCode = CODE_OPERATE_SUCCESS;
		this.datas = map;
	}

	/**
	 * 返回分页集合
	 */
	public<T extends BaseEntity> ResultDataDto(List<T> list, PageAndSort pageAndSort) {
		super();
		this.code = CODE_SUCCESS;
		this.datas = list;
		this.pageAndSort = pageAndSort;
	}

	/**
	 * 500-业务逻辑错误
	 */
	public ResultDataDto(RuntimeServiceException rex) {
		super();
		this.code = CODE_ERROR_SERVICE;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = rex.getMessage();
	}

	/**
	 * 501-功能不完善，无对应方法
	 */
	public ResultDataDto(RuntimeFunctionException rex) {
		super();
		this.code = CODE_ERROR_FUNCTION;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = rex.getMessage();
	}

	/**
	 * 502-网络异常
	 */
	public ResultDataDto(RuntimeWebException rex) {
		super();
		this.code = CODE_ERROR_WEB;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = rex.getMessage();
	}

	/**
	 * 503-未知其它
	 */
	public ResultDataDto(RuntimeOtherException rex) {
		super();
		this.code = CODE_ERROR_OTHER;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = rex.getMessage();
	}

	/**
	 * 异常
	 */
	public ResultDataDto(Exception ex) {
		super();
		this.code = CODE_ERROR_OTHER;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = getErrorMessage(ex);
		ex.printStackTrace();
	}

	/**
	 * 运行时异常
	 */
	public ResultDataDto(RuntimeException rex) {
		super();
		this.code = CODE_ERROR_OTHER;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = rex.getMessage();
	}

	/**
	 * 运行时异常
	 */
	public ResultDataDto(Throwable tx) {
		super();
		this.code = CODE_ERROR_OTHER;
		this.operateCode = CODE_OPERATE_FAILURE;
		this.message = tx.getMessage();
	}

	private String operateCode;

	/**
	 * 结果编码
	 */
	private String code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 结果数据，单个实体 或 List<T>
	 */
	private Object datas;

	/**
	 * 权限集
	 */
//	private List<BaseAuthority> authorities = new ArrayList<BaseAuthority>();

	/**
	 * 分页数据
	 */
	private PageAndSort pageAndSort;

	private static String getErrorMessage(Exception ex) {
		if (ex instanceof ArithmeticException) {
			return "系统异常：计算错误";
		}
		if (ex instanceof NullPointerException) {
			return "系统异常：输入错误，缺少输入值";
		}
		if (ex instanceof ClassCastException) {
			return "系统异常：类型转换错误";
		}
		if (ex instanceof NegativeArraySizeException) {
			return "系统异常：集合负数";
		}
		if (ex instanceof ArrayIndexOutOfBoundsException) {
			return "系统异常：集合超出范围";
		}
		if (ex instanceof FileNotFoundException) {
			return "系统异常：文件未找到";
		}
		if (ex instanceof NumberFormatException) {
			return "系统异常：输入数字错误";
		}
		if (ex instanceof SQLException) {
			return "系统异常：数据库异常";
		}
		if (ex instanceof NonTransientDataAccessException) {
			return "系统异常：数据库异常";
		}
		if (ex instanceof IOException) {
			return "系统异常：文件读写错误";
		}
		if (ex instanceof NoSuchMethodException) {
			return "系统异常：方法找不到";
		}
		if (ex instanceof BindException) {
			return "系统异常：控制器参数装配异常!";
		}
		return ex.getMessage();
	}

	public boolean isSuccessMessage() {
		return CODE_SUCCESS.equals(code);
	}

	// -------------------------- getter and setter -----------------------------
	public String getCode() {
		return code;
	}

	public ResultDataDto setCode(String code) {
		this.code = code;
		return this;
	}

	public Object getDatas() {
		return datas;
	}

	public ResultDataDto setDatas(Object datas) {
		this.datas = datas;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResultDataDto setMessage(String message) {
		this.message = message;
		return this;
	}

	public PageAndSort getPageAndSort() {
		return pageAndSort;
	}

	public void setPageAndSort(PageAndSort pageAndSort) {
		this.pageAndSort = pageAndSort;
	}

//	public List<BaseAuthority> getAuthorities() {
//		return authorities;
//	}

//	public void setAuthorities(List<BaseAuthority> authorities) {
//		this.authorities = authorities;
//	}

	public String getOperateCode() {
		return operateCode;
	}

	public ResultDataDto setOperateCode(String operateCode) {
		this.operateCode = operateCode;
		return this;
	}
}
