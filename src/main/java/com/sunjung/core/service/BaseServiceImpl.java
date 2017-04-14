package com.sunjung.core.service;

import com.sunjung.common.dto.JqgridFilters;
import com.sunjung.core.dao.BaseMapper;
import com.sunjung.core.mybatis.specification.PageAndSort;
import com.sunjung.core.dto.Pair;
import com.sunjung.core.mybatis.specification.QueryLike;
import com.sunjung.core.entity.BaseBusinessEntity;
import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.entity.annotation.BaseEntityMapper;
import com.sunjung.core.mybatis.specification.Specification;
import com.sunjung.core.util.ConstraintUtil;
import com.sunjung.core.util.EntityColumnUtil;
import com.sunjung.core.util.GenericeClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZhenWeiLai on 2017/3/26.
 */
public class BaseServiceImpl<T extends BaseEntity,M extends BaseMapper<T>> implements BaseService<T,M> {

    private Class<?> entityClass = GenericeClassUtils.getSuperClassGenricType(this.getClass(), 0);
    private Class<?> mapperClass = GenericeClassUtils.getSuperClassGenricType(this.getClass(), 1);

    @Resource(name="sqlSessionTemplate")
    private SqlSession sqlSession;

//    @Autowired
//    private INaturalIdGeneratorFactory naturalIdGeneratorFactory;

//    public BaseServiceImpl(){
//
//    }

    protected M getMapper() {
        return (M) sqlSession.getMapper(mapperClass);
    }


    @Override
    public T findEntityById(Integer id) {
        T entity = getMapper().findEntityById(new Specification<T>(entityClass, id));
//        setEntityManyToOne(entity);
        return entity;
    }

    @Override
    public T getEntityPreviousById(Integer id) {
        T entity = getMapper().getEntityPreviousById(new Specification<T>(entityClass, id));
//        setEntityManyToOne(entity);
        return entity;
    }

    @Override
    public T getEntityNextById(Integer id) {
        T entity = getMapper().getEntityNextById(new Specification<T>(entityClass, id));
//        setEntityManyToOne(entity);
        return entity;
    }

//    @Override
//    public T findEntityByNaturalId(String naturalId) {
//        NaturalIdHeader naturalIdHeader = entityClass.getAnnotation(NaturalIdHeader.class);
//        if (null == naturalIdHeader) {
//            throw new RuntimeException(entityClass.getSimpleName() + "不存在单号生成器注解：NaturalIdHeader");
//        }
//        CriteriaDto<T> criteriaDto = new CriteriaDto<T>(entityClass, naturalId);
//        criteriaDto.setPrimaryKey(naturalIdHeader.ognl());
//        T entity = getMapper().findEntityByNaturalId(criteriaDto);
//        setEntityManyToOne(entity);
//        return entity;
//    }

//    private void setEntityManyToOne(T entity) {
//        @SuppressWarnings("unchecked")
//        List<Pair<String, Class>> list = EntityColumnUtil.generateEntityManyToOne(entityClass);
//        for (@SuppressWarnings("unchecked")Pair<String, Class> pair : list) {
//            CriteriaDto<T> criteriaDto = new CriteriaDto<T>(pair.getRight());
//            ManyToOne manyToOne = (ManyToOne) EntityColumnUtil.getEntityPropertyAnnotation(entityClass, pair.getLeft(), ManyToOne.class);
//            if (null == manyToOne || ValidateUtil.isEmpty(manyToOne.name(), entity)) {
//                continue;
//            }
//            criteriaDto.addQueryLike(new QueryLike(manyToOne.refname(), LikeMode.Eq, ValidateUtil.format(manyToOne.name(), entity)));
//            criteriaDto.setFlexiPageDto(new FlexiPageDto(1, 1, "id"));
//            criteriaDto.getFlexiPageDto().setRowCount(10L);
//            String packageName = pair.getRight().getPackage().getName();
//            packageName = packageName.substring(0, packageName.lastIndexOf("."));
//            Class<?> serviceClass = null;
//            try {
//                serviceClass = Class.forName(packageName+".service.impl."+pair.getRight().getSimpleName()+"ResourceServiceImpl");
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(this.getClass().getSimpleName()+" , 无法定位 BaseEntityMapper");
//            }
//            List<T> entitys = getMapper(serviceClass).findByPage(criteriaDto);
//            if (entitys.isEmpty()) {
//                continue;
//            }
//            EntityColumnUtil.setEntityProperty(entity, pair.getLeft(), entitys.get(0));
//        }
//    }

    @Override
    public Integer addEntity(T entity) {
        validateAddEntity(entity);
        getMapper().addEntity(entity);
        return entity.getId();
    }

//    /**
//     * 设置单号
//     */
//    protected void setNaturalId(T entity) {
//        NaturalIdHeader naturalIdHeader = entityClass.getAnnotation(NaturalIdHeader.class);
//        String naturalId = generateNaturalId(entity);
//        if (null == naturalId) {
//            return;
//        }
//        if(ValidateUtil.isEmpty(naturalIdHeader.ognl(), entity)) {
//            EntityColumnUtil.setEntityProperty(entity, naturalIdHeader.ognl(), naturalId);
//        }
//    }

//    /**
//     * 生成单号
//     */
//    protected String generateNaturalId(T entity) {
//        NaturalIdHeader naturalIdHeader = entity.getClass().getAnnotation(NaturalIdHeader.class);
//        if (null == naturalIdHeader) {
//            return null;
//        }
//        INaturalIdGenerator naturalIdGenerator = naturalIdGeneratorFactory.getNaturalIdGenerator(naturalIdHeader.generatorFactoryClass());
//        Object naturalId = naturalIdGenerator.generateNaturalId(entity);
//        return naturalId.toString();
//    }

    /**
     * 子类可以重写新增验证方法
     */
    protected void validateAddEntity(T entity) {
        ConstraintUtil.setDefaultValue(entity);
        ConstraintUtil.isNotNullConstraint(entity);
    }

    @Override
    public void updateEntity(T entity) {
        validateUpdateEntity(entity);
        getMapper().updateEntity(entity);
    }

    @Override
    public void updateEntityStatus(Integer entityId, String status) {
        validateUpdateEntityStatus(entityId, status);
        BaseBusinessEntity entity;
        try {
            entity = (BaseBusinessEntity) entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("更新状态失败，类型异常。");
        }
        entity.setId(entityId);
        entity.setStatus(status);
        getMapper().updateEntityStatus(entity);
    }

    protected void validateUpdateEntityStatus(Integer entityId, String status) {

    }

    /**
     * 子类可以重写更新验证方法
     */
    protected void validateUpdateEntity(T entity) {
        ConstraintUtil.setDefaultValue(entity);
//        entity.setVersion(this.findEntityById(entity.getId()).getVersion()+1);
        ConstraintUtil.isNotNullConstraint(entity);
    }

    @Override
    public void deleteById(Integer id) {
        getMapper().deleteById(new Specification<T>(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        Specification<T> specification = new Specification<>(entityClass);
        specification.setPageAndSort(new PageAndSort(0, 0, getBaseEntityMapper().primaryKey()));
        return getMapper().findAll(specification);
    }

    @Override
    public List<T> findByLike(Specification<T> specification) {
        return getMapper().findByLike(specification);
    }

    @Override
    public T getOne(Specification<T> specification) {
        specification.setPageAndSort(new PageAndSort(1,1));
        List<T> results = getMapper().findByLike(specification);
        return results.isEmpty() ? null:results.get(0);
    }

    @Override
    public List<T> findByPage(Specification<T> specification) {
        Long rowCount = this.findRowCount(specification);
        specification.getPageAndSort().setRowCount(rowCount);
        return getMapper().findByPage(specification);
    }

    @Override
    public List<T> findByLike(T entity) {
        Specification<T> specification = makeSpecification(null);
        List<QueryLike> queryLikes = EntityColumnUtil.setEntityQueryLike(entity, specification.getQueryLikes());
        specification.setQueryLikes(queryLikes);
        return this.findByLike(specification);
    }

    @Override
    public List<T> findByPage(Pair<T, PageAndSort> pair) {
        return findByPage(pair.getLeft(), pair.getRight());
    }

    @Override
    public List<T> findByPage(T entity, PageAndSort pageAndSort) {
        Specification<T> specification = makeSpecification(pageAndSort);
        List<QueryLike> queryLikes = EntityColumnUtil.setEntityQueryLike(entity, specification.getQueryLikes());
        specification.setQueryLikes(queryLikes);
        return this.findByPage(specification);
    }

    @Override
    public List<T> findByJqgridFilters(JqgridFilters jqgridFilters, PageAndSort pageAndSort) {
        Specification<T> specification = makeSpecificationByJqgridFilters(jqgridFilters,pageAndSort);
        return this.findByPage(specification);
    }

    /**
     * 模糊搜索条件
     */
    protected Specification<T> makeSpecification(PageAndSort pageAndSort) {
        Specification<T> specification = new Specification<T>(entityClass);
        specification.setPageAndSort(pageAndSort);
        return specification;
    }

    /**
     * JQGRID 模糊查询条件
     */
    protected Specification<T> makeSpecificationByJqgridFilters(JqgridFilters jqgridFilters,PageAndSort pageAndSort) {
        Specification<T> specification = makeSpecification(pageAndSort);
        if(jqgridFilters == null || jqgridFilters.getRules() == null || jqgridFilters.getRules().isEmpty() || StringUtils.isBlank(jqgridFilters.getGroupOp()))
            return specification;
        List<JqgridFilters.Rule> rules = jqgridFilters.getRules();
        String groupOp =  jqgridFilters.getGroupOp();
        QueryLike[] queryLikes = new QueryLike[rules.size()];
        for(int i=0;i<queryLikes.length;i++){
            JqgridFilters.Rule rule = rules.get(i);
            queryLikes[i] = new QueryLike(rule.getField(),QueryLike.LikeMode.getByCode(rule.getOp()),rule.getData()).setOperator(groupOp);
        }
        specification.setQueryLikes(Arrays.asList(queryLikes));
        return specification;
    }

    @Override
    public Long findRowCount(Specification<T> pageAndSort) {
        return getMapper().findRowCount(pageAndSort);
    }

    private BaseEntityMapper getBaseEntityMapper() {
        BaseEntityMapper baseEntityMapper = entityClass.getAnnotation(BaseEntityMapper.class);
        if (null == baseEntityMapper) {
            throw new RuntimeException(this.getClass().getSimpleName()+" , 请配置注解 BaseEntityMapper");
        }
        return baseEntityMapper;
    }
}
