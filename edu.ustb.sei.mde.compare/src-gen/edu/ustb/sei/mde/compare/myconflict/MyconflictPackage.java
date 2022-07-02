/**
 */
package edu.ustb.sei.mde.compare.myconflict;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictFactory
 * @model kind="package"
 * @generated
 */
public interface MyconflictPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "myconflict";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.ustb.edu.cn/sei/mde/compare/nmc/myconflict";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "myconflict";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MyconflictPackage eINSTANCE = edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl.init();

	/**
	 * The meta object id for the '{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl <em>My Conflicts</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl
	 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl#getMyConflicts()
	 * @generated
	 */
	int MY_CONFLICTS = 0;

	/**
	 * The feature id for the '<em><b>Conflicts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICTS__CONFLICTS = 0;

	/**
	 * The feature id for the '<em><b>Base URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICTS__BASE_URI = 1;

	/**
	 * The feature id for the '<em><b>Branch UR Is</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICTS__BRANCH_UR_IS = 2;

	/**
	 * The number of structural features of the '<em>My Conflicts</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICTS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>My Conflicts</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICTS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictImpl <em>My Conflict</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyConflictImpl
	 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl#getMyConflict()
	 * @generated
	 */
	int MY_CONFLICT = 1;

	/**
	 * The feature id for the '<em><b>Tuples</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICT__TUPLES = 0;

	/**
	 * The feature id for the '<em><b>Information</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICT__INFORMATION = 1;

	/**
	 * The number of structural features of the '<em>My Conflict</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>My Conflict</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_CONFLICT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link edu.ustb.sei.mde.compare.myconflict.impl.TupleImpl <em>Tuple</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.ustb.sei.mde.compare.myconflict.impl.TupleImpl
	 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl#getTuple()
	 * @generated
	 */
	int TUPLE = 2;

	/**
	 * The feature id for the '<em><b>Branch</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE__BRANCH = 0;

	/**
	 * The feature id for the '<em><b>EObject</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE__EOBJECT = 1;

	/**
	 * The feature id for the '<em><b>EStructural Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE__ESTRUCTURAL_FEATURE = 2;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE__POSITION = 3;

	/**
	 * The number of structural features of the '<em>Tuple</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Tuple</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts <em>My Conflicts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>My Conflicts</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflicts
	 * @generated
	 */
	EClass getMyConflicts();

	/**
	 * Returns the meta object for the containment reference list '{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getConflicts <em>Conflicts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Conflicts</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflicts#getConflicts()
	 * @see #getMyConflicts()
	 * @generated
	 */
	EReference getMyConflicts_Conflicts();

	/**
	 * Returns the meta object for the attribute '{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBaseURI <em>Base URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base URI</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBaseURI()
	 * @see #getMyConflicts()
	 * @generated
	 */
	EAttribute getMyConflicts_BaseURI();

	/**
	 * Returns the meta object for the attribute list '{@link edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBranchURIs <em>Branch UR Is</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Branch UR Is</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflicts#getBranchURIs()
	 * @see #getMyConflicts()
	 * @generated
	 */
	EAttribute getMyConflicts_BranchURIs();

	/**
	 * Returns the meta object for class '{@link edu.ustb.sei.mde.compare.myconflict.MyConflict <em>My Conflict</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>My Conflict</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflict
	 * @generated
	 */
	EClass getMyConflict();

	/**
	 * Returns the meta object for the containment reference list '{@link edu.ustb.sei.mde.compare.myconflict.MyConflict#getTuples <em>Tuples</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tuples</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflict#getTuples()
	 * @see #getMyConflict()
	 * @generated
	 */
	EReference getMyConflict_Tuples();

	/**
	 * Returns the meta object for the attribute '{@link edu.ustb.sei.mde.compare.myconflict.MyConflict#getInformation <em>Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Information</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.MyConflict#getInformation()
	 * @see #getMyConflict()
	 * @generated
	 */
	EAttribute getMyConflict_Information();

	/**
	 * Returns the meta object for class '{@link edu.ustb.sei.mde.compare.myconflict.Tuple <em>Tuple</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tuple</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.Tuple
	 * @generated
	 */
	EClass getTuple();

	/**
	 * Returns the meta object for the attribute '{@link edu.ustb.sei.mde.compare.myconflict.Tuple#getBranch <em>Branch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Branch</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.Tuple#getBranch()
	 * @see #getTuple()
	 * @generated
	 */
	EAttribute getTuple_Branch();

	/**
	 * Returns the meta object for the reference '{@link edu.ustb.sei.mde.compare.myconflict.Tuple#getEObject <em>EObject</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>EObject</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.Tuple#getEObject()
	 * @see #getTuple()
	 * @generated
	 */
	EReference getTuple_EObject();

	/**
	 * Returns the meta object for the reference '{@link edu.ustb.sei.mde.compare.myconflict.Tuple#getEStructuralFeature <em>EStructural Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>EStructural Feature</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.Tuple#getEStructuralFeature()
	 * @see #getTuple()
	 * @generated
	 */
	EReference getTuple_EStructuralFeature();

	/**
	 * Returns the meta object for the attribute list '{@link edu.ustb.sei.mde.compare.myconflict.Tuple#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Position</em>'.
	 * @see edu.ustb.sei.mde.compare.myconflict.Tuple#getPosition()
	 * @see #getTuple()
	 * @generated
	 */
	EAttribute getTuple_Position();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MyconflictFactory getMyconflictFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl <em>My Conflicts</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyConflictsImpl
		 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl#getMyConflicts()
		 * @generated
		 */
		EClass MY_CONFLICTS = eINSTANCE.getMyConflicts();

		/**
		 * The meta object literal for the '<em><b>Conflicts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MY_CONFLICTS__CONFLICTS = eINSTANCE.getMyConflicts_Conflicts();

		/**
		 * The meta object literal for the '<em><b>Base URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MY_CONFLICTS__BASE_URI = eINSTANCE.getMyConflicts_BaseURI();

		/**
		 * The meta object literal for the '<em><b>Branch UR Is</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MY_CONFLICTS__BRANCH_UR_IS = eINSTANCE.getMyConflicts_BranchURIs();

		/**
		 * The meta object literal for the '{@link edu.ustb.sei.mde.compare.myconflict.impl.MyConflictImpl <em>My Conflict</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyConflictImpl
		 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl#getMyConflict()
		 * @generated
		 */
		EClass MY_CONFLICT = eINSTANCE.getMyConflict();

		/**
		 * The meta object literal for the '<em><b>Tuples</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MY_CONFLICT__TUPLES = eINSTANCE.getMyConflict_Tuples();

		/**
		 * The meta object literal for the '<em><b>Information</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MY_CONFLICT__INFORMATION = eINSTANCE.getMyConflict_Information();

		/**
		 * The meta object literal for the '{@link edu.ustb.sei.mde.compare.myconflict.impl.TupleImpl <em>Tuple</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.ustb.sei.mde.compare.myconflict.impl.TupleImpl
		 * @see edu.ustb.sei.mde.compare.myconflict.impl.MyconflictPackageImpl#getTuple()
		 * @generated
		 */
		EClass TUPLE = eINSTANCE.getTuple();

		/**
		 * The meta object literal for the '<em><b>Branch</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TUPLE__BRANCH = eINSTANCE.getTuple_Branch();

		/**
		 * The meta object literal for the '<em><b>EObject</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE__EOBJECT = eINSTANCE.getTuple_EObject();

		/**
		 * The meta object literal for the '<em><b>EStructural Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE__ESTRUCTURAL_FEATURE = eINSTANCE.getTuple_EStructuralFeature();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TUPLE__POSITION = eINSTANCE.getTuple_Position();

	}

} //MyconflictPackage
