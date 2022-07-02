/**
 */
package edu.ustb.sei.mde.compare.myconflict.impl;

import edu.ustb.sei.mde.compare.myconflict.MyConflict;
import edu.ustb.sei.mde.compare.myconflict.MyConflicts;
import edu.ustb.sei.mde.compare.myconflict.MyconflictPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>My Conflicts</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl#getConflicts <em>Conflicts</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl#getBaseURI <em>Base URI</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl#getBranchURIs <em>Branch UR Is</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MyConflictsImpl extends MinimalEObjectImpl.Container implements MyConflicts {
	/**
	 * The cached value of the '{@link #getConflicts() <em>Conflicts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConflicts()
	 * @generated
	 * @ordered
	 */
	protected EList<MyConflict> conflicts;

	/**
	 * The default value of the '{@link #getBaseURI() <em>Base URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseURI()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseURI() <em>Base URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseURI()
	 * @generated
	 * @ordered
	 */
	protected String baseURI = BASE_URI_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBranchURIs() <em>Branch UR Is</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBranchURIs()
	 * @generated
	 * @ordered
	 */
	protected EList<String> branchURIs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MyConflictsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MyconflictPackage.Literals.MY_CONFLICTS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MyConflict> getConflicts() {
		if (conflicts == null) {
			conflicts = new EObjectContainmentEList<MyConflict>(MyConflict.class, this, MyconflictPackage.MY_CONFLICTS__CONFLICTS);
		}
		return conflicts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBaseURI() {
		return baseURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseURI(String newBaseURI) {
		String oldBaseURI = baseURI;
		baseURI = newBaseURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MyconflictPackage.MY_CONFLICTS__BASE_URI, oldBaseURI, baseURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getBranchURIs() {
		if (branchURIs == null) {
			branchURIs = new EDataTypeUniqueEList<String>(String.class, this, MyconflictPackage.MY_CONFLICTS__BRANCH_UR_IS);
		}
		return branchURIs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MyconflictPackage.MY_CONFLICTS__CONFLICTS:
				return ((InternalEList<?>)getConflicts()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MyconflictPackage.MY_CONFLICTS__CONFLICTS:
				return getConflicts();
			case MyconflictPackage.MY_CONFLICTS__BASE_URI:
				return getBaseURI();
			case MyconflictPackage.MY_CONFLICTS__BRANCH_UR_IS:
				return getBranchURIs();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MyconflictPackage.MY_CONFLICTS__CONFLICTS:
				getConflicts().clear();
				getConflicts().addAll((Collection<? extends MyConflict>)newValue);
				return;
			case MyconflictPackage.MY_CONFLICTS__BASE_URI:
				setBaseURI((String)newValue);
				return;
			case MyconflictPackage.MY_CONFLICTS__BRANCH_UR_IS:
				getBranchURIs().clear();
				getBranchURIs().addAll((Collection<? extends String>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MyconflictPackage.MY_CONFLICTS__CONFLICTS:
				getConflicts().clear();
				return;
			case MyconflictPackage.MY_CONFLICTS__BASE_URI:
				setBaseURI(BASE_URI_EDEFAULT);
				return;
			case MyconflictPackage.MY_CONFLICTS__BRANCH_UR_IS:
				getBranchURIs().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MyconflictPackage.MY_CONFLICTS__CONFLICTS:
				return conflicts != null && !conflicts.isEmpty();
			case MyconflictPackage.MY_CONFLICTS__BASE_URI:
				return BASE_URI_EDEFAULT == null ? baseURI != null : !BASE_URI_EDEFAULT.equals(baseURI);
			case MyconflictPackage.MY_CONFLICTS__BRANCH_UR_IS:
				return branchURIs != null && !branchURIs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (baseURI: ");
		result.append(baseURI);
		result.append(", branchURIs: ");
		result.append(branchURIs);
		result.append(')');
		return result.toString();
	}

} //MyConflictsImpl
