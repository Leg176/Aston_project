package ru.practicum.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.practicum.entity.User;
import ru.practicum.util.HibernateSessionFactoryUtil;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User saveUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            return user;
        } catch (HibernateException e) {
            log.error("Ошибка сохранения пользователя", e);
            throw new RuntimeException("Ошибка сохранения пользователя", e);
        }
    }

    @Override
    public User updateUser(User user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User mergeUser = (User) session.merge(user);
            tx.commit();
            return mergeUser;
        } catch (HibernateException e) {
            log.error("Ошибка обновления данных пользователя", e);
            throw new RuntimeException("Ошибка обновления данных пользователя", e);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> getUsers(List<Long> ids) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User u WHERE u.id IN :ids", User.class);
            query.setParameter("ids", ids);
            return query.getResultList();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);

            if (user != null) {
                session.delete(user);
                tx.commit();
                return true;
            }
            tx.commit();
            return false;
        } catch (HibernateException e) {
            log.error("Ошибка удаления пользователя", e);
            throw new RuntimeException("Ошибка удаления пользователя", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        }
    }

    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.id = :id", Long.class);
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        }
    }
}
