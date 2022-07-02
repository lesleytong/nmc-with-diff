/**
 */
package edu.ustb.sei.mde.compare.myconflict;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictPackage
 * @generated
 */
public interface MyconflictFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MyconflictFactory eINSTANCE = edu.ustb.sei.mde.compare.myconflict.impl.MyconflictFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>My Conflicts</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>My Conflicts</em>'.
	 * @generated
	 */
	MyConflicts createMyConflicts();

	/**
	 * Returns a new object of class '<em>My Conflict</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>My Conflict</em>'.
	 * @generated
	 */
	MyConflict createMyConflict();

	/**
	 * Returns a new object of class '<em>Tuple</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Tuple</em>'.
	 * @generated
	 */
	Tuple createTuple();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MyconflictPackage getMyconflictPackage();

} //MyconflictFactory
