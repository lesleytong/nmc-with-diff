package edu.ustb.sei.mde.compare.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ElementMutator {
  public final EClass type;

  public double featureChangeRate = 0.1;

  public double featureValueChangeRate = 0.4;

  public int minChangedFeatures = 1;

  private int minChangedFeatureValues = 1;

  private double possOfElementRemoval = 0.4;

  private double possOfElementInsertion = 0.5;

  private double possOfElementReorder = 0.1;

  private double possOfValueSet = 0.9;

  private double possOfValueUnset = 0.1;

  private double possOfCharRemoval = 0.2;

  private double possOfCharAlter = 0.2;

  private double possOfCharInsert = 0.7;

  private final RandomUtils random = new RandomUtils();

  private final Map<EObject, EObject> mapping = new HashMap<EObject, EObject>();

  private final ArrayList<EObject> objectsOfType = new ArrayList<EObject>();

  private final List<EStructuralFeature> features;

  private final Set<EClass> focusedTypes;

  private final Map<EClass, List<EObject>> focusedObjects;

  private List<EObject> original;

  public ElementMutator(final EClass type) {
    this.type = type;
    final Function1<EStructuralFeature, Boolean> _function = new Function1<EStructuralFeature, Boolean>() {
      public Boolean apply(final EStructuralFeature it) {
        return Boolean.valueOf((!((((it.isDerived() || it.isTransient()) || it.isVolatile()) || (Boolean.valueOf(it.isChangeable()) == Boolean.valueOf(false))) || ((it instanceof EReference) && ElementMutator.this.isContainerRef(((EReference) it))))));
      }
    };
    this.features = IterableExtensions.<EStructuralFeature>toList(IterableExtensions.<EStructuralFeature>filter(type.getEAllStructuralFeatures(), _function));
    final Function1<EStructuralFeature, Boolean> _function_1 = new Function1<EStructuralFeature, Boolean>() {
      public Boolean apply(final EStructuralFeature it) {
        return Boolean.valueOf((it instanceof EReference));
      }
    };
    final Function1<EStructuralFeature, EReference> _function_2 = new Function1<EStructuralFeature, EReference>() {
      public EReference apply(final EStructuralFeature it) {
        return ((EReference) it);
      }
    };
    final Function1<EReference, EClass> _function_3 = new Function1<EReference, EClass>() {
      public EClass apply(final EReference it) {
        return it.getEReferenceType();
      }
    };
    this.focusedTypes = IterableExtensions.<EClass>toSet(IterableExtensions.<EReference, EClass>map(IterableExtensions.<EStructuralFeature, EReference>map(IterableExtensions.<EStructuralFeature>filter(this.features, _function_1), _function_2), _function_3));
    HashMap<EClass, List<EObject>> _hashMap = new HashMap<EClass, List<EObject>>();
    this.focusedObjects = _hashMap;
  }

  protected void focusIfNeeded(final EObject o) {
    final Consumer<EClass> _function = new Consumer<EClass>() {
      public void accept(final EClass t) {
        boolean _isSuperTypeOf = t.isSuperTypeOf(o.eClass());
        if (_isSuperTypeOf) {
          final Function<EClass, List<EObject>> _function = new Function<EClass, List<EObject>>() {
            public List<EObject> apply(final EClass it) {
              return new ArrayList<EObject>();
            }
          };
          final List<EObject> list = ElementMutator.this.focusedObjects.computeIfAbsent(t, _function);
          list.add(o);
        }
      }
    };
    this.focusedTypes.forEach(_function);
  }

  public boolean isContainerRef(final EReference ref) {
    boolean _isContainer = ref.isContainer();
    if (_isContainer) {
      return true;
    } else {
      EReference _eOpposite = ref.getEOpposite();
      boolean _tripleNotEquals = (_eOpposite != null);
      if (_tripleNotEquals) {
        return this.isContainmentRef(ref.getEOpposite());
      } else {
        return false;
      }
    }
  }

  public boolean isContainmentRef(final EReference ref) {
    boolean _isContainment = ref.isContainment();
    if (_isContainment) {
      return true;
    } else {
      final EAnnotation anno = ref.getEAnnotation("subsets");
      if ((anno == null)) {
        return false;
      } else {
        final Function1<EObject, Boolean> _function = new Function1<EObject, Boolean>() {
          public Boolean apply(final EObject it) {
            return Boolean.valueOf((it instanceof EReference));
          }
        };
        final Function1<EObject, EReference> _function_1 = new Function1<EObject, EReference>() {
          public EReference apply(final EObject it) {
            return ((EReference) it);
          }
        };
        final Function1<EReference, Boolean> _function_2 = new Function1<EReference, Boolean>() {
          public Boolean apply(final EReference it) {
            return Boolean.valueOf(ElementMutator.this.isContainmentRef(it));
          }
        };
        return IterableExtensions.<EReference>exists(IterableExtensions.<EObject, EReference>map(IterableExtensions.<EObject>filter(anno.getReferences(), _function), _function_1), _function_2);
      }
    }
  }

  public void prepare(final List<EObject> contents) {
//    Collection<EObject> _copyAll = EcoreUtil.<EObject>copyAll(contents);
//    this.original = ((List<EObject>) _copyAll);
//    this.init(this.original);
	  
	    this.original = contents;
	    this.init(this.original);
  }
  

  protected void buildMapping(final List<EObject> original, final List<EObject> copy) {
    int _size = original.size();
    int _size_1 = copy.size();
    boolean _tripleNotEquals = (_size != _size_1);
    if (_tripleNotEquals) {
      throw new RuntimeException();
    }
    final Iterator<EObject> oit = original.iterator();
    final Iterator<EObject> cit = copy.iterator();
    while (oit.hasNext()) {
      {
        final EObject o = oit.next();
        final EObject c = cit.next();
        
        // lyt: test
//        System.out.println(o);
//        System.out.println(c);
        
        this.mapping.put(o, c);
        this.buildMapping(o.eContents(), c.eContents());
      }
    }
  }

  protected void init(final List<EObject> contents) {
    for (final EObject r : contents) {
      {
        boolean _isSuperTypeOf = this.type.isSuperTypeOf(r.eClass());
        if (_isSuperTypeOf) {
          this.objectsOfType.add(r);
        }
        this.focusIfNeeded(r);
        final Consumer<EObject> _function = new Consumer<EObject>() {
          public void accept(final EObject c) {
            boolean _isSuperTypeOf = ElementMutator.this.type.isSuperTypeOf(c.eClass());
            if (_isSuperTypeOf) {
              ElementMutator.this.objectsOfType.add(c);
            }
            ElementMutator.this.focusIfNeeded(c);
          }
        };
        r.eAllContents().forEachRemaining(_function);
      }
    }
  }

  public void push() {
    Collection<EObject> _copyAll = EcoreUtil.<EObject>copyAll(this.original);
    final List<EObject> copiedForMutation = ((List<EObject>) _copyAll);
    this.mapping.clear();
    this.buildMapping(this.original, copiedForMutation);
  }

  protected void randomEditList(final List<Object> list, final Supplier<?> randomValue) {
    final int size = list.size();
    long _round = Math.round((this.featureValueChangeRate * size));
    int changes = Math.max(((int) _round), this.minChangedFeatureValues);
    final List<Double> actionRates = Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(this.possOfElementRemoval), Double.valueOf(this.possOfElementInsertion), Double.valueOf(this.possOfElementReorder)));
    for (int i = 0; ((i < list.size()) && (changes > 0)); i++) {
      boolean _shouldHappen = this.random.shouldHappen(this.featureValueChangeRate);
      if (_shouldHappen) {
        final int action = this.random.select(((double[])Conversions.unwrapArray(actionRates, double.class)));
        switch (action) {
          case 0:
            list.remove(i);
            i--;
            break;
          case 1:
            final Object v = randomValue.get();
            try {
              if ((v != null)) {
                list.add(i, v);
                i++;
              }
            } catch (final Throwable _t) {
              if (_t instanceof Exception) {
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
            break;
          case 2:
            final int id = this.random.nextInt(list.size());
            try {
              if ((id != i)) {
                if ((id > i)) {
                  final Object tar = list.remove(id);
                  final Object src = list.remove(i);
                  list.add(i, tar);
                  list.add(id, src);
                } else {
                  final Object src_1 = list.remove(i);
                  final Object tar_1 = list.remove(id);
                  list.add(id, src_1);
                  list.add(i, tar_1);
                }
              }
            } catch (final Throwable _t) {
              if (_t instanceof Exception) {
                changes++;
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
            break;
        }
        changes--;
      }
    }
    while ((changes > 0)) {
      {
        final Object value = randomValue.get();
        if ((value != null)) {
          list.add(value);
        }
        changes--;
      }
    }
  }

  protected Object randomEdit(final Object oldValue, final Function<Object, Object> randomValue) {
    Object _xifexpression = null;
    if ((oldValue == null)) {
      _xifexpression = randomValue.apply(null);
    } else {
      Object _xblockexpression = null;
      {
        final List<Double> actionRates = Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(this.possOfValueSet), Double.valueOf(this.possOfValueUnset)));
        final int action = this.random.select(((double[])Conversions.unwrapArray(actionRates, double.class)));
        Object _xifexpression_1 = null;
        if ((action == 0)) {
          _xifexpression_1 = randomValue.apply(oldValue);
        } else {
          _xifexpression_1 = null;
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }

  public boolean randomEdit(final Boolean b) {
    boolean _xifexpression = false;
    if ((b == null)) {
      _xifexpression = this.random.nextBoolean();
    } else {
      _xifexpression = (!(b).booleanValue());
    }
    return _xifexpression;
  }

  public int randomEdit(final Integer c) {
    int _xifexpression = (int) 0;
    if ((c == null)) {
      _xifexpression = this.random.nextInt(100);
    } else {
      int _xblockexpression = (int) 0;
      {
        long _round = Math.round((this.featureValueChangeRate * (c).intValue()));
        final int range = Math.max(this.minChangedFeatureValues, ((int) _round));
        final int offset = this.random.nextInt((-range), range);
        _xblockexpression = ((c).intValue() + offset);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }

  public double randomEdit(final Double c) {
    double _xifexpression = (double) 0;
    if ((c == null)) {
      _xifexpression = this.random.nextDouble(100);
    } else {
      double _xblockexpression = (double) 0;
      {
        final double range = Math.max(this.minChangedFeatureValues, (this.featureValueChangeRate * (c).doubleValue()));
        final double offset = this.random.nextDouble((-range), range);
        _xblockexpression = ((c).doubleValue() + offset);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }

  public String randomEdit(final String string) {
    if ((string == null)) {
      Object _randomValue = this.random.randomValue(EcorePackage.eINSTANCE.getEString());
      return ((String) _randomValue);
    }
    final List<Double> actionPoss = Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(this.possOfCharRemoval), Double.valueOf(this.possOfCharAlter), Double.valueOf(this.possOfCharInsert)));
    int _length = string.length();
    double _multiply = (this.featureValueChangeRate * _length);
    long _round = Math.round(_multiply);
    int numberOfChanges = Math.max(this.minChangedFeatureValues, ((int) _round));
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; (i < string.length()); i++) {
      if ((this.random.shouldHappen(this.featureValueChangeRate) && (numberOfChanges > 0))) {
        final int action = this.random.select(((double[])Conversions.unwrapArray(actionPoss, double.class)));
        switch (action) {
          case 0:
            break;
          case 1:
            builder.append(this.random.nextChar());
            break;
          case 2:
            builder.append(this.random.nextChar());
            i--;
            break;
        }
        numberOfChanges--;
      } else {
        builder.append(string.charAt(i));
      }
    }
    while ((numberOfChanges > 0)) {
      {
        builder.append(this.random.nextChar());
        numberOfChanges--;
      }
    }
    return builder.toString();
  }

  public Object randomValue(final EDataType type, final Object oldValue) {
    Object _xblockexpression = null;
    {
      Class<?> instanceClass = type.getInstanceClass();
      Object _xifexpression = null;
      if (((instanceClass == int.class) || (instanceClass == Integer.class))) {
        _xifexpression = Integer.valueOf(this.randomEdit(((Integer) oldValue)));
      } else {
        Object _xifexpression_1 = null;
        if (((instanceClass == boolean.class) || (instanceClass == Boolean.class))) {
          _xifexpression_1 = Boolean.valueOf(this.randomEdit(((Boolean) oldValue)));
        } else {
          Object _xifexpression_2 = null;
          if ((instanceClass == String.class)) {
            _xifexpression_2 = this.randomEdit(((String) oldValue));
          } else {
            Double _xifexpression_3 = null;
            if (((instanceClass == double.class) || (instanceClass == double.class))) {
              _xifexpression_3 = Double.valueOf(this.randomEdit(((Double) oldValue)));
            } else {
              _xifexpression_3 = null;
            }
            _xifexpression_2 = _xifexpression_3;
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = ((Object)_xifexpression_1);
      }
      _xblockexpression = ((Object)_xifexpression);
    }
    return _xblockexpression;
  }

  protected void mutate(final EObject originalObject) {
    final EObject object = this.mapping.get(originalObject);
    int _size = this.features.size();
    double _multiply = (this.featureChangeRate * _size);
    long _max = Math.max(this.minChangedFeatures, Math.round(_multiply));
    int numOfChangedFeatures = ((int) _max);
    int retry = 0;
    while (((numOfChangedFeatures > 0) && (retry < 5))) {
      {
        final Set<EStructuralFeature> featuresToBeChanged = this.random.<EStructuralFeature>select(this.features, numOfChangedFeatures);
        for (final EStructuralFeature feature : featuresToBeChanged) {
          try {
            final Object oldValue = object.eGet(feature);
            if ((feature instanceof EReference)) {
              final List<EObject> focusedObjects = this.focusedObjects.getOrDefault(((EReference)feature).getEReferenceType(), Collections.<EObject>emptyList());
              boolean _isMany = ((EReference)feature).isMany();
              if (_isMany) {
                boolean _isEmpty = focusedObjects.isEmpty();
                if (_isEmpty) {
                  numOfChangedFeatures++;
                } else {
                  final Supplier<Object> _function = new Supplier<Object>() {
                    public Object get() {
                      EObject _xblockexpression = null;
                      {
                        boolean _isEmpty = focusedObjects.isEmpty();
                        if (_isEmpty) {
                          return null;
                        }
                        final EObject oo = ElementMutator.this.random.<EObject>selectOne(focusedObjects);
                        EObject _xifexpression = null;
                        if (((oo != null) && (!(ElementMutator.this.isContainmentRef(((EReference)feature)) && ElementMutator.this.isContainerOf(oo, originalObject))))) {
                          _xifexpression = ElementMutator.this.mapping.get(oo);
                        } else {
                          _xifexpression = null;
                        }
                        _xblockexpression = _xifexpression;
                      }
                      return _xblockexpression;
                    }
                  };
                  this.randomEditList(((List<Object>) oldValue), _function);
                }
              } else {
                final Function<Object, Object> _function_1 = new Function<Object, Object>() {
                  public Object apply(final Object it) {
                    EObject _xblockexpression = null;
                    {
                      boolean _isEmpty = focusedObjects.isEmpty();
                      if (_isEmpty) {
                        return null;
                      }
                      final EObject oo = ElementMutator.this.random.<EObject>selectOne(focusedObjects);
                      EObject _xifexpression = null;
                      if (((oo != null) && (!(ElementMutator.this.isContainmentRef(((EReference)feature)) && ElementMutator.this.isContainerOf(oo, originalObject))))) {
                        _xifexpression = ElementMutator.this.mapping.get(oo);
                      } else {
                        _xifexpression = null;
                      }
                      _xblockexpression = _xifexpression;
                    }
                    return _xblockexpression;
                  }
                };
                object.eSet(feature, this.randomEdit(oldValue, _function_1));
              }
              numOfChangedFeatures--;
            } else {
              final EDataType eAttributeType = ((EAttribute) feature).getEAttributeType();
              boolean _isMany_1 = feature.isMany();
              if (_isMany_1) {
                final Supplier<Object> _function_2 = new Supplier<Object>() {
                  public Object get() {
                    return ElementMutator.this.random.randomValue(eAttributeType);
                  }
                };
                this.randomEditList(((List<Object>) oldValue), _function_2);
              } else {
                final Function<Object, Object> _function_3 = new Function<Object, Object>() {
                  public Object apply(final Object it) {
                    return ElementMutator.this.randomValue(eAttributeType, it);
                  }
                };
                final Object value = this.randomEdit(oldValue, _function_3);
                if ((value == null)) {
                  object.eUnset(feature);
                } else {
                  object.eSet(feature, value);
                }
              }
              numOfChangedFeatures--;
            }
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
        retry++;
      }
    }
  }

  public boolean isContainerOf(final EObject o1, final EObject o2) {
    boolean _xblockexpression = false;
    {
      EObject o = o2;
      while ((o != null)) {
        {
          if ((o == o1)) {
            return true;
          }
          o = o.eContainer();
        }
      }
      _xblockexpression = false;
    }
    return _xblockexpression;
  }

  public List<EObject> selectAll() {
    return this.objectsOfType;
  }

  public Set<EObject> select(final int num) {
    final HashSet<EObject> selection = new HashSet<EObject>();
    int retry = 0;
    while ((selection.size() < num)) {
      {
        final int oldSize = selection.size();
        int i = this.random.nextInt(this.objectsOfType.size());
        selection.add(this.objectsOfType.get(i));
        int _size = selection.size();
        boolean _tripleEquals = (oldSize == _size);
        if (_tripleEquals) {
          retry++;
          if ((retry >= 10)) {
            return selection;
          }
        } else {
          retry = 0;
        }
      }
    }
    return selection;
  }

  public EObject getMutant(final EObject object) {
    return this.mapping.get(object);
  }

  public void doMutation(final EObject object) {
    this.push();
    this.mutate(object);
  }
}
