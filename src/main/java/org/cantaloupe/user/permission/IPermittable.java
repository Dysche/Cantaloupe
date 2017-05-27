package org.cantaloupe.user.permission;

public interface IPermittable {
	public void grantPermission(String node);
	
	public void revokePermission(String node);
}