/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.diff;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import edu.ustb.sei.mde.compare.AttributeChange;
import edu.ustb.sei.mde.compare.CompareFactory;
import edu.ustb.sei.mde.compare.DifferenceKind;
import edu.ustb.sei.mde.compare.DifferenceSource;
import edu.ustb.sei.mde.compare.FeatureMapChange;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.MatchResource;
import edu.ustb.sei.mde.compare.ReferenceChange;
import edu.ustb.sei.mde.compare.ResourceAttachmentChange;

/**
 * This default implementation of an {@link IDiffProcessor} will build the necessary differences and attach
 * them to the appropriate {@link Match}.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DiffBuilder implements IDiffProcessor {
	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.diff.IDiffProcessor#referenceChange(edu.ustb.sei.mde.compare.Match,
	 *      org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EObject,
	 *      edu.ustb.sei.mde.compare.DifferenceKind, edu.ustb.sei.mde.compare.DifferenceSource)
	 */
	public void referenceChange(Match match, EReference reference, EObject value, DifferenceKind kind,
			DifferenceSource source) {
		final ReferenceChange referenceChange = CompareFactory.eINSTANCE.createReferenceChange();
		referenceChange.setMatch(match);
		referenceChange.setReference(reference);
		referenceChange.setValue(value);
		referenceChange.setKind(kind);
		referenceChange.setSource(source);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.diff.IDiffProcessor#attributeChange(edu.ustb.sei.mde.compare.Match,
	 *      org.eclipse.emf.ecore.EAttribute, java.lang.Object, edu.ustb.sei.mde.compare.DifferenceKind,
	 *      edu.ustb.sei.mde.compare.DifferenceSource)
	 */
	public void attributeChange(Match match, EAttribute attribute, Object value, DifferenceKind kind,
			DifferenceSource source) {
		final AttributeChange attributeChange = CompareFactory.eINSTANCE.createAttributeChange();
		attributeChange.setMatch(match);
		attributeChange.setAttribute(attribute);
		attributeChange.setValue(value);
		attributeChange.setKind(kind);
		attributeChange.setSource(source);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.diff.IDiffProcessor#featureMapChange(edu.ustb.sei.mde.compare.Match,
	 *      org.eclipse.emf.ecore.EAttribute, java.lang.Object, edu.ustb.sei.mde.compare.DifferenceKind,
	 *      edu.ustb.sei.mde.compare.DifferenceSource)
	 * @since 3.2
	 */
	public void featureMapChange(Match match, EAttribute attribute, Object value, DifferenceKind kind,
			DifferenceSource source) {
		final FeatureMapChange featureMapChange = CompareFactory.eINSTANCE.createFeatureMapChange();
		featureMapChange.setMatch(match);
		featureMapChange.setAttribute(attribute);
		featureMapChange.setValue(value);
		featureMapChange.setKind(kind);
		featureMapChange.setSource(source);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.diff.IDiffProcessor#resourceAttachmentChange(edu.ustb.sei.mde.compare.Match,
	 *      java.lang.String, edu.ustb.sei.mde.compare.DifferenceKind,
	 *      edu.ustb.sei.mde.compare.DifferenceSource)
	 */
	public void resourceAttachmentChange(Match match, String uri, DifferenceKind kind,
			DifferenceSource source) {
		final ResourceAttachmentChange change = CompareFactory.eINSTANCE.createResourceAttachmentChange();
		change.setMatch(match);
		change.setResourceURI(uri);
		change.setKind(kind);
		change.setSource(source);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.IDiffProcessor#resourceLocationChange(org.eclipse.emf.compare.
	 *      MatchResource, java.lang.String, java.lang.String org.eclipse.emf.compare.DifferenceKind,
	 *      org.eclipse.emf.compare.DifferenceSource)
	 * @deprecated {@link edu.ustb.sei.mde.compare.ResourceLocationChange}s have been replaced by
	 *             {@link ResourceAttachmentChange}s of kind Move.
	 */
	@Deprecated
	public void resourceLocationChange(MatchResource matchResource, String baseLocation,
			String changedLocation, DifferenceKind kind, DifferenceSource source) {
		// Nothing to do here.
	}
}
