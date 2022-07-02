/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.conflict;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static edu.ustb.sei.mde.compare.ConflictKind.PSEUDO;
import static edu.ustb.sei.mde.compare.ConflictKind.REAL;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import edu.ustb.sei.mde.compare.AttributeChange;
import edu.ustb.sei.mde.compare.CompareFactory;
import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.Conflict;
import edu.ustb.sei.mde.compare.ConflictKind;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceKind;
import edu.ustb.sei.mde.compare.DifferenceSource;
import edu.ustb.sei.mde.compare.EMFCompareMessages;
import edu.ustb.sei.mde.compare.Equivalence;
import edu.ustb.sei.mde.compare.FeatureMapChange;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.MatchResource;
import edu.ustb.sei.mde.compare.ResourceAttachmentChange;
import edu.ustb.sei.mde.compare.internal.ThreeWayTextDiff;
import edu.ustb.sei.mde.compare.utils.ReferenceUtil;

/**
 * Class in charge of finding conflicting diffs for a given diff of type T.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <T>
 *            The type of diff for which conflict are researched
 */
public abstract class AbstractConflictSearch<T extends Diff> {

	/** The difference, never <code>null</code>. */
	protected final T diff;

	/** The comparison that contains diff. */
	protected final Comparison comparison;

	/** The index of the comparison. */
	protected final ComparisonIndex index;

	/** The monitor to report progress to. */
	protected final Monitor monitor;

	/**
	 * Constructor.
	 * 
	 * @param diff
	 *            The diff to search conflicts with, must not be <code>null</code> and have a non-null match
	 *            that belongs to a non-null comparison. It must also have a non-null {@link DifferenceKind}
	 *            and {@link DifferenceSource}.
	 * @param index
	 *            Comparison index, must not be null
	 * @param monitor
	 *            the monitor to report progress to, must not be null
	 */
	public AbstractConflictSearch(T diff, ComparisonIndex index, Monitor monitor) {
		checkNotNull(diff);
		if (diff.getMatch() == null || diff.getMatch().getComparison() == null) {
			throw new IllegalArgumentException();
		}
		comparison = diff.getMatch().getComparison();
		checkArgument(diff.getKind() != null && diff.getSource() != null);
		this.diff = diff;
		this.index = checkNotNull(index);
		this.monitor = checkNotNull(monitor);
	}

	/**
	 * Detect conflicts with {@link AbstractConflictSearch#diff} in its comparison. This will add or update
	 * conflicts in <code>diff</code>'s comparison.
	 */
	public abstract void detectConflicts();

	/**
	 * Get the diffs in the same {@link Match} as diff.
	 * 
	 * @return A never-null EList of differences in the same {@link Match} as diff, including diff.
	 */
	protected EList<Diff> getDiffsInSameMatch() {
		return diff.getMatch().getDifferences();
	}

	/**
	 * Specifies whether the given {@code diff1} and {@code diff2} are either {@link FeatureMapChange feature
	 * map changes} or mergeable {@link AttributeChange attribute changes} of String attributes.
	 * 
	 * @param diff1
	 *            One of the diffs to check.
	 * @param diff2
	 *            The other diff to check.
	 * @return <code>true</code> if it is a {@link FeatureMapChange} or a mergeable {@link AttributeChange},
	 *         <code>false</code> otherwise.
	 */
	protected boolean isFeatureMapChangeOrMergeableStringAttributeChange(Diff diff1, Diff diff2) {
		return isFeatureMapChange(diff1) || areMergeableStringAttributeChanges(diff1, diff2);
	}

	/**
	 * Specifies whether the given {@code diff} is a {@link FeatureMapChange}.
	 * 
	 * @param toCheck
	 *            The diff to check.
	 * @return <code>true</code> if it is a {@link FeatureMapChange}, <code>false</code> otherwise.
	 */
	protected boolean isFeatureMapChange(Diff toCheck) {
		return toCheck instanceof FeatureMapChange;
	}

	/**
	 * Specifies whether the two given diffs, {@code diff1} and {@code diff2}, are both {@link AttributeChange
	 * attribute changes} of String attributes and can be merged with a line-based three-way merge.
	 * 
	 * @see edu.ustb.sei.mde.compare.internal.ThreeWayTextDiff
	 * @param diff1
	 *            One of the diffs to check.
	 * @param diff2
	 *            The other diff to check.
	 * @return <code>true</code> if the diffs are mergeable changes of a string attribute, <code>false</code>
	 *         otherwise.
	 */
	protected boolean areMergeableStringAttributeChanges(Diff diff1, Diff diff2) {
		final boolean mergeableStringAttributeChange;
		if (isStringAttributeChange(diff1)) {
			final AttributeChange attributeChange1 = (AttributeChange)diff1;
			final AttributeChange attributeChange2 = (AttributeChange)diff2;
			mergeableStringAttributeChange = isMergeable(attributeChange1, attributeChange2);
		} else {
			mergeableStringAttributeChange = false;
		}
		return mergeableStringAttributeChange;
	}

	/**
	 * Specifies whether the given {@code diff} is a {@link AttributeChange} of a String attribute.
	 * 
	 * @param toCheck
	 *            The diff to check.
	 * @return <code>true</code> if it is a {@link AttributeChange} of a String attribute, <code>false</code>
	 *         otherwise.
	 */
	protected boolean isStringAttributeChange(Diff toCheck) {
		return toCheck instanceof AttributeChange && ((AttributeChange)toCheck).getAttribute()
				.getEAttributeType().getInstanceClass() == String.class;
	}

	/**
	 * Specifies whether the two given attribute changes, {@code diff1} and {@code diff2}, can be merged with
	 * a line-based three-way merge.
	 * 
	 * @see edu.ustb.sei.mde.compare.internal.ThreeWayTextDiff
	 * @param diff1
	 *            One of the attribute changes to check.
	 * @param diff2
	 *            The other attribute change to check.
	 * @return <code>true</code> if the attribute changes are mergeable, <code>false</code> otherwise.
	 */
	protected boolean isMergeable(final AttributeChange diff1, final AttributeChange diff2) {
		final String changedValue1 = getChangedValue(diff1);
		final String changedValue2 = getChangedValue(diff2);
		final EObject originalContainer = diff1.getMatch().getOrigin();
		final EAttribute changedAttribute = diff1.getAttribute();
		final String originalValue = (String)ReferenceUtil.safeEGet(originalContainer, changedAttribute);
		return isMergeableText(changedValue1, changedValue2, originalValue);
	}

	/**
	 * Specifies whether the given three versions of a text {@code left}, {@code right}, and {@code origin}
	 * are mergeable with a line-based three-way merge.
	 * 
	 * @param left
	 *            The left version.
	 * @param right
	 *            The right version.
	 * @param origin
	 *            The original version.
	 * @return <code>true</code> if they are mergeable, false otherwise.
	 * @since 3.2
	 */
	protected boolean isMergeableText(final String left, final String right, final String origin) {
		ThreeWayTextDiff textDiff = new ThreeWayTextDiff(origin, left, right);
		return !textDiff.isConflicting();
	}

	/**
	 * Returns the changed attribute value denoted by the given {@code diff}.
	 * 
	 * @param attributeChange
	 *            The attribute change for which the changed value is requested.
	 * @return The changed attribute value.
	 */
	protected String getChangedValue(final AttributeChange attributeChange) {
		final String changedValue;
		Match match = attributeChange.getMatch();
		if (DifferenceSource.LEFT.equals(attributeChange.getSource())) {
			changedValue = (String)ReferenceUtil.safeEGet(match.getLeft(), attributeChange.getAttribute());
		} else if (DifferenceSource.RIGHT.equals(attributeChange.getSource())) {
			changedValue = (String)ReferenceUtil.safeEGet(match.getRight(), attributeChange.getAttribute());
		} else {
			changedValue = (String)attributeChange.getValue();
		}
		return changedValue;
	}

	/**
	 * This will be called whenever we detect a new conflict in order to create (or update) the actual
	 * association.
	 * 
	 * @param other
	 *            Second of the two differences for which we detected a conflict.
	 * @param kind
	 *            Kind of this conflict.
	 */
	protected void conflict(Diff other, ConflictKind kind) {
		// Pre-condition: diff and other are not already part of the same conflict
		if (diff.getConflict() != null && diff.getConflict().getDifferences().contains(other)) {
			return;
		}

		Conflict conflict = null;
		Conflict toBeMerged = null;
		if (diff.getConflict() != null) {
			conflict = diff.getConflict();
			if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
			if (other.getConflict() != null) {
				// Merge the two
				toBeMerged = other.getConflict();
			}
		} else if (other.getConflict() != null) {
			conflict = other.getConflict();
			if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
				conflict.setKind(kind);
			}
		} else if (diff.getEquivalence() != null) {
			Equivalence equivalence = diff.getEquivalence();
			for (Diff equ : equivalence.getDifferences()) {
				if (equ.getConflict() != null) {
					conflict = equ.getConflict();
					if (other.getConflict() == conflict) {
						// See initial pre-condition
						return;
					}
					if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
						conflict.setKind(kind);
					}
					if (other.getConflict() != null) {
						// Merge the two
						toBeMerged = other.getConflict();
					}
					break;
				}
			}
		} else if (other.getEquivalence() != null) {
			Equivalence equivalence = other.getEquivalence();
			for (Diff equ : equivalence.getDifferences()) {
				if (equ.getConflict() != null) {
					conflict = equ.getConflict();
					if (conflict.getKind() == PSEUDO && conflict.getKind() != kind) {
						conflict.setKind(kind);
					}
					break;
				}
			}
		}

		if (conflict == null) {
			conflict = CompareFactory.eINSTANCE.createConflict();
			conflict.setKind(kind);
			comparison.getConflicts().add(conflict);
		}

		final EList<Diff> conflictDiffs = conflict.getDifferences();
		if (toBeMerged != null) {
			// These references are opposite. We can't simply iterate
			for (Diff aDiff : Lists.newArrayList(toBeMerged.getDifferences())) {
				conflictDiffs.add(aDiff);
			}
			if (toBeMerged.getKind() == REAL && conflict.getKind() != REAL) {
				conflict.setKind(REAL);
			}
			EcoreUtil.remove(toBeMerged);
			toBeMerged.getDifferences().clear();
		}

		conflict.getDifferences().add(diff);
		conflict.getDifferences().add(other);
	}

	/**
	 * Returns the MatchResource corresponding to the given <code>resource</code>.
	 * 
	 * @param resource
	 *            Resource for which we need a MatchResource.
	 * @return The MatchResource corresponding to the given <code>resource</code>.
	 */
	protected MatchResource getMatchResource(Resource resource) {
		final List<MatchResource> matchedResources = comparison.getMatchedResources();
		final int size = matchedResources.size();
		MatchResource soughtMatch = null;
		for (int i = 0; i < size && soughtMatch == null; i++) {
			final MatchResource matchRes = matchedResources.get(i);
			if (matchRes.getRight() == resource || matchRes.getLeft() == resource
					|| matchRes.getOrigin() == resource) {
				soughtMatch = matchRes;
			}
		}
		checkState(soughtMatch != null, EMFCompareMessages
				.getString("ResourceAttachmentChangeSpec.MissingMatch", resource.getURI().lastSegment())); //$NON-NLS-1$
		return soughtMatch;
	}

	/**
	 * Provide the model element the given diff applies to.
	 * 
	 * @param rac
	 *            The change
	 * @return The model element of the given diff, or null if it cannot be found.
	 */
	protected EObject getRelatedModelElement(ResourceAttachmentChange rac) {
		Match m = rac.getMatch();
		EObject o;
		switch (rac.getSource()) {
			case LEFT:
				o = m.getLeft(); // null if DELETE
				break;
			case RIGHT:
				o = m.getRight(); // null if DELETE
				break;
			default:
				o = null;
		}
		return o;
	}

	/**
	 * Provide the non-null model element the given diff applies to.
	 * 
	 * @param rac
	 *            The change
	 * @return The model element of the given diff, cannot be null.
	 */
	protected EObject getValue(ResourceAttachmentChange rac) {
		Match m = rac.getMatch();
		EObject o;
		switch (rac.getKind()) {
			case ADD:
				// Voluntary pass-through
			case CHANGE:
				// Voluntary pass-through
			case MOVE:
				switch (rac.getSource()) {
					case LEFT:
						o = m.getLeft();
						break;
					case RIGHT:
						o = m.getRight();
						break;
					default:
						o = null;
				}
				break;
			case DELETE:
				o = m.getOrigin();
				break;
			default:
				throw new IllegalStateException();
		}
		checkState(o != null);
		return o;
	}

	// FIXME Move this elsewhere
	/**
	 * This predicate will be <code>true</code> for any Match which represents a containment deletion.
	 * 
	 * @return A Predicate that will be met by containment deletions.
	 */
	protected Predicate<? super Match> isContainmentDelete() {
		return new Predicate<Match>() {
			public boolean apply(Match input) {
				return input.getOrigin() != null && (input.getLeft() == null || input.getRight() == null);
			}
		};
	}
}
