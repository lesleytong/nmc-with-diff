/**
 */
package edu.ustb.sei.mde.compare.myconflict.impl;

import edu.ustb.sei.mde.compare.myconflict.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MyconflictFactoryImpl extends EFactoryImpl implements MyconflictFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MyconflictFactory init() {
		try {
			MyconflictFactory theMyconflictFactory = (MyconflictFactory)EPackage.Registry.INSTANCE.getEFactory(MyconflictPackage.eNS_URI);
			if (theMyconflictFactory != null) {
				return theMyconflictFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MyconflictFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MyconflictFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MyconflictPackage.MY_CONFLICTS: return createMyConflicts();
			case MyconflictPackage.MY_CONFLICT: return createMyConflict();
			case MyconflictPackage.TUPLE: return createTuple();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MyConflicts createMyConflicts() {
		MyConflictsImpl myConflicts = new MyConflictsImpl();
		return myConflicts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MyConflict createMyConflict() {
		MyConflictImpl myConflict = new MyConflictImpl();
		return myConflict;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Tuple createTuple() {
		TupleImpl tuple = new TupleImpl();
		return tuple;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MyconflictPackage getMyconflictPackage() {
		return (MyconflictPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MyconflictPackage getPackage() {
		return MyconflictPackage.eINSTANCE;
	}

} //MyconflictFactoryImpl
