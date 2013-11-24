package br.com.libutils.jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractRepository<T extends Serializable> implements
		Repository<T> {

	protected final Class<? extends T> entityConcreteClass;

	/**
	 * Creates a new repository instance.
	 * 
	 * @param entityConcreteClass
	 *            Concrete class of the entity. Notice that this class is hidden
	 *            behind the {@link Repository} interface
	 */
	protected AbstractRepository(final Class<? extends T> entityConcreteClass) {
		this.entityConcreteClass = entityConcreteClass;
	}

	/**
	 * Gets the current entity manager.
	 * <p/>
	 * Sub-classes should implement this method according to their needs. This
	 * is specially useful when multiple persistence units are used.
	 * 
	 * @return entity manager
	 */
	protected abstract EntityManager getEntityManager();

	protected CriteriaBuilder createCriteriaBuilder() {
		return getEntityManager().getCriteriaBuilder();
	}

	@SuppressWarnings("unchecked")
	protected CriteriaQuery<T> createCriteriaQuery(final CriteriaBuilder cb) {
		return (CriteriaQuery<T>) cb.createQuery(this.entityConcreteClass);
	}

	@SuppressWarnings("unchecked")
	protected Root<T> createRoot(final CriteriaQuery<T> cq) {
		return (Root<T>) cq.from(this.entityConcreteClass);
	}

	@Override
	public void create(T c) {
		getEntityManager().persist(c);
	}

	@Override
	public T retrieve(Long id) {
		return getEntityManager().find(this.entityConcreteClass, id);
	}

	@Override
	public T update(T c) {
		return getEntityManager().merge(c);
	}

	@Override
	public void delete(T c) {
		getEntityManager().remove(c);
	}

	@Override
	public void refresh(T c) {
		getEntityManager().refresh(c);
	}

	protected List<T> retrieveByCriteria(CriteriaQuery<T> cq) {

		if (cq == null) {
			cq = createCriteriaQuery(createCriteriaBuilder());

			final Root<T> root = createRoot(cq);
			cq.select(root);
		}

		final TypedQuery<T> type = getEntityManager().createQuery(cq);

		return type.getResultList();
	}

}
