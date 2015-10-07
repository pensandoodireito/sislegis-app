package br.gov.mj.sislegis.app.parser;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper para evitar ter que iterar sobre todo o resultado e converter. <br>
 * Esta classe encapsula uma lista de K e retorna um iterator de E.
 * 
 * @author coutinho
 * 
 *
 */
public abstract class CollectionLazyConverter<E, K> extends AbstractCollection<E> {
	private Iterator<E> iterator;
	private Iterator<K> materiasIterator;
	private int size = 0;

	public CollectionLazyConverter(List<K> materias) {
		super();
		size = materias.size();
		this.materiasIterator = materias.iterator();
		iterator = new Iterator<E>() {
			@Override
			public boolean hasNext() {

				return materiasIterator.hasNext();
			}

			@Override
			public E next() {
				return convertKtoE(materiasIterator.next());
			}
		};

	}

	protected abstract E convertKtoE(K next);

	@Override
	public Iterator<E> iterator() {
		return iterator;
	}

	@Override
	public int size() {
		return size;
	}
}
