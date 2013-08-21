package br.com.libutils.jpa;

import java.io.Serializable;

public interface Repository<T extends Serializable> {

	void create(T c);

	T retrieve(Long id);

	T update(T c);

	void delete(T c);

	void refresh(T c);
	
}
