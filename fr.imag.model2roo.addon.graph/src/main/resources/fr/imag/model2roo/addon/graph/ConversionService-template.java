
    @org.springframework.beans.factory.annotation.Autowired
    __TOP_PACKAGE__.repository.__ENTITY__Repository __ENTITY_LOWER__repository;

     public org.springframework.core.convert.converter.Converter<Long, __TOP_PACKAGE__.domain.__ENTITY__> getLongTo__ENTITY__Converter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, __TOP_PACKAGE__.domain.__ENTITY__>() {
            public __TOP_PACKAGE__.domain.__ENTITY__ convert(Long id) {
                return __ENTITY_LOWER__repository.findOne(id);
            }
        };
    }