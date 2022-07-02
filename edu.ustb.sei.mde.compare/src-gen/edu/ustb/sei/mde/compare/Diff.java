/**
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package edu.ustb.sei.mde.compare;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Diff</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A DiffElement describes a difference related to the EObjects mapped by its parent MatchElement.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getMatch <em>Match</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getRequires <em>Requires</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getRequiredBy <em>Required By</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getImplies <em>Implies</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getImpliedBy <em>Implied By</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getRefines <em>Refines</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getRefinedBy <em>Refined By</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getPrimeRefining <em>Prime Refining</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getKind <em>Kind</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getSource <em>Source</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getState <em>State</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getEquivalence <em>Equivalence</em>}</li>
 *   <li>{@link edu.ustb.sei.mde.compare.Diff#getConflict <em>Conflict</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff()
 * @model
 * @generated
 */
public interface Diff extends EObject {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Match</b></em>' container reference. It is bidirectional and its
	 * opposite is '{@link edu.ustb.sei.mde.compare.Match#getDifferences <em>Differences</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Back-reference towards this
	 * difference's parent match, it points towards the mapping on which this difference has been detected.
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Match</em>' container reference.
	 * @see #setMatch(Match)
	 * @model derived="true"
	 * @generated
	 */
	Match getMatch();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.Diff#getMatch <em>Match</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Match</em>' reference.
	 * @see #getMatch()
	 * @generated
	 */
	void setMatch(Match value);

	/**
	 * Returns the value of the '<em><b>Requires</b></em>' reference list. The list contents are of type
	 * {@link edu.ustb.sei.mde.compare.Diff}. It is bidirectional and its opposite is '
	 * {@link edu.ustb.sei.mde.compare.Diff#getRequiredBy <em>Required By</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> This will reference all differences that should be merged
	 * before this one. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Requires</em>' reference list.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Requires()
	 * @see edu.ustb.sei.mde.compare.Diff#getRequiredBy
	 * @model opposite="requiredBy"
	 * @generated
	 */
	EList<Diff> getRequires();

	/**
	 * Returns the value of the '<em><b>Required By</b></em>' reference list. The list contents are of type
	 * {@link edu.ustb.sei.mde.compare.Diff}. It is bidirectional and its opposite is '
	 * {@link edu.ustb.sei.mde.compare.Diff#getRequires <em>Requires</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> This will reference all differences that depend on this one
	 * for their merging. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Required By</em>' reference list.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_RequiredBy()
	 * @see edu.ustb.sei.mde.compare.Diff#getRequires
	 * @model opposite="requires"
	 * @generated
	 */
	EList<Diff> getRequiredBy();

	/**
	 * Returns the value of the '<em><b>Implies</b></em>' reference list.
	 * The list contents are of type {@link edu.ustb.sei.mde.compare.Diff}.
	 * It is bidirectional and its opposite is '{@link edu.ustb.sei.mde.compare.Diff#getImpliedBy <em>Implied By</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implies</em>' reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implies</em>' reference list.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Implies()
	 * @see edu.ustb.sei.mde.compare.Diff#getImpliedBy
	 * @model opposite="impliedBy"
	 * @generated
	 */
	EList<Diff> getImplies();

	/**
	 * Returns the value of the '<em><b>Implied By</b></em>' reference list.
	 * The list contents are of type {@link edu.ustb.sei.mde.compare.Diff}.
	 * It is bidirectional and its opposite is '{@link edu.ustb.sei.mde.compare.Diff#getImplies <em>Implies</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implied By</em>' reference list isn't clear, there really should be more of
	 * a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implied By</em>' reference list.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_ImpliedBy()
	 * @see edu.ustb.sei.mde.compare.Diff#getImplies
	 * @model opposite="implies"
	 * @generated
	 */
	EList<Diff> getImpliedBy();

	/**
	 * Returns the value of the '<em><b>Refines</b></em>' reference list. The list contents are of type
	 * {@link edu.ustb.sei.mde.compare.Diff}. It is bidirectional and its opposite is '
	 * {@link edu.ustb.sei.mde.compare.Diff#getRefinedBy <em>Refined By</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> This can be used to create "high level" differences to
	 * regroup one or more diffs in a common "container". For example, this could be used to regroup <i>n</i>
	 * technical differences into one single semantic difference, such as the differences
	 * "profile application added" and "reference to stereotype added" that can actually be regrouped under a
	 * single, more understandable "stereotype application added". <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Refines</em>' reference list.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Refines()
	 * @see edu.ustb.sei.mde.compare.Diff#getRefinedBy
	 * @model opposite="refinedBy"
	 * @generated
	 */
	EList<Diff> getRefines();

	/**
	 * Returns the value of the '<em><b>Refined By</b></em>' reference list. The list contents are of type
	 * {@link edu.ustb.sei.mde.compare.Diff}. It is bidirectional and its opposite is '
	 * {@link edu.ustb.sei.mde.compare.Diff#getRefines <em>Refines</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> This will reference the higher-level difference that shadows
	 * this one, if any. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Refined By</em>' reference list.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_RefinedBy()
	 * @see edu.ustb.sei.mde.compare.Diff#getRefines
	 * @model opposite="refines"
	 * @generated
	 */
	EList<Diff> getRefinedBy();

	/**
	 * Returns the value of the '<em><b>Prime Refining</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prime Refining</em>' reference isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prime Refining</em>' reference.
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_PrimeRefining()
	 * @model transient="true" changeable="false" derived="true"
	 * @generated
	 */
	Diff getPrimeRefining();

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute. The literals are from the enumeration
	 * {@link edu.ustb.sei.mde.compare.DifferenceKind}. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Describes the kind of this difference, whether it is an addition, deletion, change,
	 * or move. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see edu.ustb.sei.mde.compare.DifferenceKind
	 * @see #setKind(DifferenceKind)
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Kind()
	 * @model required="true"
	 * @generated
	 */
	DifferenceKind getKind();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.Diff#getKind <em>Kind</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Kind</em>' attribute.
	 * @see edu.ustb.sei.mde.compare.DifferenceKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(DifferenceKind value);

	/**
	 * Returns the value of the '<em><b>Conflict</b></em>' reference. It is bidirectional and its opposite is
	 * '{@link edu.ustb.sei.mde.compare.Conflict#getDifferences <em>Differences</em>}'. <!-- begin-user-doc -->
	 * <!-- end-user-doc --> <!-- begin-model-doc --> If this element is in conflict with one (or more) other
	 * differences, this will reference the Conflict association. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Conflict</em>' reference.
	 * @see #setConflict(Conflict)
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Conflict()
	 * @see edu.ustb.sei.mde.compare.Conflict#getDifferences
	 * @model opposite="differences"
	 * @generated
	 */
	Conflict getConflict();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.Diff#getConflict <em>Conflict</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conflict</em>' reference.
	 * @see #getConflict()
	 * @generated
	 */
	void setConflict(Conflict value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute. The literals are from the enumeration
	 * {@link edu.ustb.sei.mde.compare.DifferenceSource}. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Returns the source of this diff, either one of DifferenceSource.LEFT (for two-way
	 * comparisons or differences detected between the left and origin elements) or DifferenceSource.RIGHT
	 * (for differences between the right and origin elements). <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see edu.ustb.sei.mde.compare.DifferenceSource
	 * @see #setSource(DifferenceSource)
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Source()
	 * @model required="true"
	 * @generated
	 */
	DifferenceSource getSource();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.Diff#getSource <em>Source</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Source</em>' attribute.
	 * @see edu.ustb.sei.mde.compare.DifferenceSource
	 * @see #getSource()
	 * @generated
	 */
	void setSource(DifferenceSource value);

	/**
	 * Returns the value of the '<em><b>State</b></em>' attribute. The literals are from the enumeration
	 * {@link edu.ustb.sei.mde.compare.DifferenceState}. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Returns the current state of this diff, either one of DifferenceState.UNRESOLVED
	 * (Diff is still in its initial state), DifferenceState.MERGED when the Diff has been merged or
	 * DifferenceState.DISCARDED if the user chose to ignore this difference. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>State</em>' attribute.
	 * @see edu.ustb.sei.mde.compare.DifferenceState
	 * @see #setState(DifferenceState)
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_State()
	 * @model required="true"
	 * @generated
	 */
	DifferenceState getState();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.Diff#getState <em>State</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>State</em>' attribute.
	 * @see edu.ustb.sei.mde.compare.DifferenceState
	 * @see #getState()
	 * @generated
	 */
	void setState(DifferenceState value);

	/**
	 * Returns the value of the '<em><b>Equivalence</b></em>' reference. It is bidirectional and its opposite
	 * is '{@link edu.ustb.sei.mde.compare.Equivalence#getDifferences <em>Differences</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> If this diff is equivalent to
	 * another, we will reference the equivalence from here. For example, if a reference has an eOpposite, we
	 * will detect one diff for each side of the bidirectional reference, yet merging one of these diffs is
	 * enough. We consider them equivalent. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Equivalence</em>' reference.
	 * @see #setEquivalence(Equivalence)
	 * @see edu.ustb.sei.mde.compare.ComparePackage#getDiff_Equivalence()
	 * @see edu.ustb.sei.mde.compare.Equivalence#getDifferences
	 * @model opposite="differences"
	 * @generated
	 */
	Equivalence getEquivalence();

	/**
	 * Sets the value of the '{@link edu.ustb.sei.mde.compare.Diff#getEquivalence <em>Equivalence</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Equivalence</em>' reference.
	 * @see #getEquivalence()
	 * @generated
	 */
	void setEquivalence(Equivalence value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> This will merge the diff from
	 * left to right. <!-- end-model-doc -->
	 * 
	 * @model
	 * @generated
	 */
	@Deprecated
	void copyRightToLeft();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> This will merge the diff from
	 * right to left. <!-- end-model-doc -->
	 * 
	 * @model
	 * @generated
	 */
	@Deprecated
	void copyLeftToRight();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> This will remove the diff from
	 * the model, effectively "forgetting" about it. <!-- end-model-doc -->
	 * 
	 * @model
	 * @generated
	 */
	void discard();

} // Diff
