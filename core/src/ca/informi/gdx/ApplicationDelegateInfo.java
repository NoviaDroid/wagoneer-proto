package ca.informi.gdx;

class ApplicationDelegateInfo {
	public ApplicationDelegate delegate;
	boolean added;
	boolean ready;

	public ApplicationDelegateInfo(final ApplicationDelegate delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof ApplicationDelegateInfo)) return false;
		return delegate.equals(((ApplicationDelegateInfo) obj).delegate);
	}
}