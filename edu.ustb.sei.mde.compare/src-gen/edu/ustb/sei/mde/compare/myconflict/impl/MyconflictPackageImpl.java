/**
 */
package edu.ustb.sei.mde.compare.myconflict.impl;

import edu.ustb.sei.mde.compare.myconflict.MyConflict;
import edu.ustb.sei.mde.compare.myconflict.MyConflicts;
import edu.ustb.sei.mde.compare.myconflict.MyconflictFactory;
import edu.ustb.sei.mde.compare.myconflict.MyconflictPackage;
import edu.ustb.sei.mde.compare.myconflict.Tuple;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MyconflictPackageImpl extends EPackageImpl implements MyconflictPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass myConflictsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass myConflictEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass tupleEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see edu.ustb.sei.mde.compare.myconflict.MyconflictPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MyconflictPackageImpl() {
		super(eNS_URI, MyconflictFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link MyconflictPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MyconflictPackage init() {
		if (isInited) return (MyconflictPackage)EPackage.Registry.INSTANCE.getEPackage(MyconflictPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredMyconflictPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		MyconflictPackageImpl theMyconflictPackage = registeredMyconflictPackage instanceof MyconflictPackageImpl ? (MyconflictPackageImpl)registeredMyconflictPackage : new MyconflictPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theMyconflictPackage.createPackageContents();

		// Initialize created meta-data
		theMyconflictPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMyconflictPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MyconflictPackage.eNS_URI, theMyconflictPackage);
		return theMyconflictPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMyConflicts() {
		return myConflictsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMyConflicts_Conflicts() {
		return (EReference)myConflictsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMyConflicts_BaseURI() {
		return (EAttribute)myConflictsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMyConflicts_BranchURIs() {
		return (EAttribute)myConflictsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMyConflict() {
		return myConflictEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMyConflict_Tuples() {
		return (EReference)myConflictEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMyConflict_Information() {
		return (EAttribute)myConflictEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTuple() {
		return tupleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTuple_Branch() {
		return (EAttribute)tupleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTuple_EObject() {
		return (EReference)tupleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTuple_EStructuralFeature() {
		return (EReference)tupleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTuple_Position() {
		return (EAttribute)tupleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MyconflictFactory getMyconflictFactory() {
		return (MyconflictFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		myConflictsEClass = createEClass(MY_CONFLICTS);
		createEReference(myConflictsEClass, MY_CONFLICTS__CONFLICTS);
		createEAttribute(myConflictsEClass, MY_CONFLICTS__BASE_URI);
		createEAttribute(myConflictsEClass, MY_CONFLICTS__BRANCH_UR_IS);

		myConflictEClass = createEClass(MY_CONFLICT);
		createEReference(myConflictEClass, MY_CONFLICT__TUPLES);
		createEAttribute(myConflictEClass, MY_CONFLICT__INFORMATION);

		tupleEClass = createEClass(TUPLE);
		createEAttribute(tupleEClass, TUPLE__BRANCH);
		createEReference(tupleEClass, TUPLE__EOBJECT);
		createEReference(tupleEClass, TUPLE__ESTRUCTURAL_FEATURE);
		createEAttribute(tupleEClass, TUPLE__POSITION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(myConflictsEClass, MyConflicts.class, "MyConflicts", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMyConflicts_Conflicts(), this.getMyConflict(), null, "conflicts", null, 0, -1, MyConflicts.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMyConflicts_BaseURI(), ecorePackage.getEString(), "baseURI", null, 0, 1, MyConflicts.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMyConflicts_BranchURIs(), ecorePackage.getEString(), "branchURIs", null, 0, -1, MyConflicts.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(myConflictEClass, MyConflict.class, "MyConflict", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMyConflict_Tuples(), this.getTuple(), null, "tuples", null, 0, -1, MyConflict.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMyConflict_Information(), ecorePackage.getEString(), "information", null, 0, 1, MyConflict.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(tupleEClass, Tuple.class, "Tuple", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTuple_Branch(), ecorePackage.getEInt(), "branch", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTuple_EObject(), ecorePackage.getEObject(), null, "eObject", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTuple_EStructuralFeature(), ecorePackage.getEStructuralFeature(), null, "eStructuralFeature", null, 0, 1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTuple_Position(), ecorePackage.getEInt(), "position", null, 0, -1, Tuple.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //MyconflictPackageImpl
