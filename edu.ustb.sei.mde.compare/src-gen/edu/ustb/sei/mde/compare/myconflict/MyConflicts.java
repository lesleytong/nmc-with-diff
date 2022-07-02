/**
 */
package edu.ustb.sei.mde.compare.myconflict;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>My Conflicts</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getConflicts <em>Conflicts</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBaseURI <em>Base URI</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBranchURIs <em>Branch UR Is</em>}</li>
 * </ul>
 *
 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictPackage#getMyConflicts()
 * @model
 * @generated
 */
public interface MyConflicts extends EObject {
	/**
	 * Returns the value of the '<em><b>Conflicts</b></em>' containment reference list.
	 * The list contents are of type {@link edu.ustb.sei.mde.compare.myconflict.MyConflict}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conflicts</em>' containment reference list.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictPackage#getMyConflicts_Conflicts()
	 * @model containment="true"
	 * @generated
	 */
	EList<MyConflict> getConflicts();

	/**
	 * Returns the value of the '<em><b>Base URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base URI</em>' attribute.
	 * @see #setBaseURI(String)
	 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictPackage#getMyConflicts_BaseURI()
	 * @model
	 * @generated
	 */
	String getBaseURI();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBaseURI <em>Base URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base URI</em>' attribute.
	 * @see #getBaseURI()
	 * @generated
	 */
	void setBaseURI(String value);

	/**
	 * Returns the value of the '<em><b>Branch UR Is</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Branch UR Is</em>' attribute list.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictPackage#getMyConflicts_BranchURIs()
	 * @model
	 * @generated
	 */
	EList<String> getBranchURIs();

} // MyConflicts
