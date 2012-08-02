package org.es4j.eventstore.core.dispatcher;

import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;


public class DelegateMessageDispatcher implements IDispatchCommits {

    //private static final Action<Commit> dispatch;
    private /*static*/ final DispatcherDelegate<Commit> delegate;

    public DelegateMessageDispatcher(DispatcherDelegate<Commit>/*Action<Commit>*/ delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() {
        dispose();
    }
    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    // virtual
    protected void dispose(boolean disposing) {
        // no op
    }

    @Override  // virtual
    public void dispatch(Commit commit) {
        this.delegate.dispatch(commit);
    }
}


/*
	public class DelegateMessageDispatcher : IDispatchCommits {
		private readonly Action<Commit> dispatch;

		public DelegateMessageDispatcher(Action<Commit> dispatch) {
			this.dispatch = dispatch;
		}

		public void Dispose() {
			this.Dispose(true);
			GC.SuppressFinalize(this);
		}
                
		protected virtual void Dispose(bool disposing) {
			// no op
		}

		public virtual void Dispatch(Commit commit) {
			this.dispatch(commit);
		}
	}
 */