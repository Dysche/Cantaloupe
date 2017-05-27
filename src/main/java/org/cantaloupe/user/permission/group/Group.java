package org.cantaloupe.user.permission.group;

import java.util.ArrayList;
import java.util.Collection;

import org.cantaloupe.text.Text;
import org.cantaloupe.user.permission.IPermissionHolder;
import org.cantaloupe.user.permission.IPermittable;

public abstract class Group implements IPermittable, IPermissionHolder {
	private ArrayList<String> permissions = new ArrayList<String>();

	public abstract void initialize();

	@Override
	public boolean hasPermission(String node) {
		return this.permissions.contains(node);
	}

	@Override
	public void grantPermission(String node) {
		this.permissions.add(node);
	}

	@Override
	public void revokePermission(String node) {
		this.permissions.remove(node);
	}

	public boolean isDefault() {
		return false;
	}

	public abstract String getName();

	public abstract Text getPrefix();

	public abstract Text getDescription();

	public Collection<String> getPermissions() {
		return this.permissions;
	}
}