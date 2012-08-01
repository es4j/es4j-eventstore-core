package org.es4j.eventstore.core;
/*
	public class ImmutableCollection<T> implements Collection<T> //, ICollection
	{
		private final Object         lock = new Object();
		private final Collection<T> inner;

		public ImmutableCollection(Collection<T> inner)
		{
			this.inner = inner;
		}

		public int getCount()
		{
			return this.inner.size();
		}
		public Object getSyncRoot()
		{
			return this.lock;
		}
		public boolean isSynchronized()
		{
			return false;
		}
		public boolean isReadOnly()
		{
			return true;
		}

		public Iterator<T> getEnumerator()
		{
			return this.inner.iterator();
		}
		IEnumerator IEnumerable.getEnumerator()
		{
			return this.getEnumerator();
		}

		public void add(T item)
		{
			throw new NotSupportedException(Resources.ReadOnlyCollection());
		}
		public boolean remove(T item)
		{
			throw new NotSupportedException(Resources.ReadOnlyCollection());
		}
		public void clear()
		{
			throw new NotSupportedException(Resources.ReadOnlyCollection());
		}

		public boolean contains(T item)
		{
			return this.inner.contains(item);
		}
		public void copyTo(T[] array, int arrayIndex)
		{
			this.inner.copyTo(array, arrayIndex);
		}
		public void copyTo(Array array, int index)
		{
			this.CopyTo(array.Cast<T>().ToArray(), index);
		}
}
* */