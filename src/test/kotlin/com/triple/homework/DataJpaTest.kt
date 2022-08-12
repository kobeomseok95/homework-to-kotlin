package com.triple.homework

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import javax.persistence.EntityManager

@DataJpaTest
@ActiveProfiles("test")
abstract class DataJpaTest {

    @Autowired
    protected val entityManager: EntityManager? = null

    protected fun flushAndClear() {
        entityManager?.flush()
        entityManager?.clear()
    }
}