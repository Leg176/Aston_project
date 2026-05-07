package ru.practicum.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.practicum.entity.User;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(HibernateSessionFactoryUtil.class);

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
                log.info("SessionFactory создан!");
            } catch (Exception e) {
                log.error("Ошибка инициализации SessionFactory ", e);
                throw new ExceptionInInitializerError("Ошибка инициализации " + e.getMessage());
            }
        }
        return sessionFactory;
    }
}
