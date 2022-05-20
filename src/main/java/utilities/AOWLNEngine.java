package utilities;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import model.*;
import org.semanticweb.owlapi.model.*;
import org.swrlapi.builtins.arguments.SWRLVariableBuiltInArgument;

import java.rmi.server.UID;
import java.util.*;

/**
 * Created by Thomas Farrenkopf on 07.06.2017.
 */
public class AOWLNEngine {

    public String stringSplit(String s) {
        String[] parts = s.split("#");
        String part2 = parts[parts.length - 1];
        parts = part2.split(">");
        part2 = parts[0];
        parts = part2.split("xsd");
        String finalString = parts[0];
        //finalString = finalString.replaceAll("[^a-zA-Z0-9]", "");
        finalString = finalString.replace("^", "");
        finalString = finalString.replace("\"", "");
        return finalString;
    }

    public ArrayList<CustomSWRLAtom> createSWRLAtomsForTree(HashSet<SWRLAtom> ruleSegment) {
        HashSet<CustomSWRLAtom> classAtoms = new HashSet<>();
        HashSet<CustomSWRLAtom> builtInAtoms = new HashSet<>();
        HashSet<CustomSWRLAtom> dataPropertyAtoms = new HashSet<>();
        HashSet<CustomSWRLAtom> objectPropertyAtoms = new HashSet<>();

        ArrayList swrlAtomsForTree = new ArrayList();

        for (SWRLAtom element : ruleSegment) {
            if (element instanceof SWRLClassAtom) {
                String label = stringSplit(element.getPredicate().toString());
                String key = stringSplit(((SWRLClassAtom) element).getArgument().toString());
                label = label +"\n("+key+")";
                ClassAtomCustom classAtom = new ClassAtomCustom(key, label);
                classAtoms.add(classAtom);

            } else if (element instanceof SWRLDataPropertyAtom) {
                String label = stringSplit(element.getPredicate().toString());
                String key = stringSplit(((SWRLDataPropertyAtom) element).getSecondArgument().toString());
                String firstArgument = stringSplit(((SWRLDataPropertyAtom) element).getFirstArgument().toString());
                DataPropertyAtomCustom dataPropAtom = new DataPropertyAtomCustom(firstArgument, key, label);
                dataPropertyAtoms.add(dataPropAtom);
            } else if (element instanceof SWRLBuiltInAtom) {
                //bodyBuiltInAtoms.add(element);
                //   String label = stringSplit(element.getPredicate().toString())+stringSplit(((SWRLBuiltInAtom) element).getArguments().toArray()[1].toString());
                //    String key = stringSplit(((SWRLBuiltInAtom) element).getArguments().toArray()[1].toString());
                String key = "BI" + new UID().toString();
                String label = stringSplit(element.getPredicate().toString());
                boolean isBound = false;

                List<String> arguments = new ArrayList<>();
                List<String> literals = new ArrayList<>();
                List<SWRLArgument> swrlArgumentList = new ArrayList<>(element.getAllArguments());

                for (int i = 0; i < swrlArgumentList.size(); i++) {

                    if (swrlArgumentList.get(i) instanceof SWRLVariableBuiltInArgument) {
                        SWRLVariableBuiltInArgument swrlVariableBuiltInArgument = (SWRLVariableBuiltInArgument) swrlArgumentList.get(i);

                        if (i == 0 && swrlVariableBuiltInArgument.isBound()) {
                            isBound = true;
                        }

                        arguments.add(swrlVariableBuiltInArgument.getVariableName());
                    } else {
                        String s = ((SWRLDArgument) swrlArgumentList.get(i)).toString();
                        literals.add(s);
                    }
                }
                BuiltInAtomCustom builtInAtom = new BuiltInAtomCustom(key, label, isBound, arguments.toArray(new String[arguments.size()]));
                builtInAtom.setLiterals(literals);
                builtInAtoms.add(builtInAtom);

            } else if (element instanceof SWRLObjectPropertyAtom) {
                //bodyObjectPropertyAtoms.add(element);
                String label = stringSplit(element.getPredicate().toString());
                String key = stringSplit(((SWRLObjectPropertyAtom) element).getSecondArgument().toString());
                String firstArgument = stringSplit(((SWRLObjectPropertyAtom) element).getFirstArgument().toString());
                ObjectPropertyAtomCustom objectPropAtom = new ObjectPropertyAtomCustom(firstArgument, key, label);
                objectPropertyAtoms.add(objectPropAtom);
            }
        }

        for (CustomSWRLAtom atom : classAtoms) {
            swrlAtomsForTree.add(atom);
        }
        for (CustomSWRLAtom atom : dataPropertyAtoms) {
            swrlAtomsForTree.add(atom);
        }
        for (CustomSWRLAtom atom : objectPropertyAtoms) {
            swrlAtomsForTree.add(atom);
        }
        for (CustomSWRLAtom atom : builtInAtoms) {
            swrlAtomsForTree.add(atom);
        }
        return swrlAtomsForTree;
    }


    public GraphListsForViz megaAlgorithmus(List<CustomSWRLAtom> ruleFragment) {
        HashMap<String, AOWLNElement> aowlnElements;
        HashMap<String, AOWLNEdgeElement> aowlnEdges;
        aowlnEdges = new HashMap<>();
        aowlnElements = new HashMap<>();

        String key = null;
        String label = null;
        AOWLNElementTypeEnum elementTypeEnum = null;
        AOWLNElement newElement = null;
        AOWLNEdgeElement newEdgeElement = null;

        //Classes, data and object props
        List<CustomSWRLAtom> standardAtoms = new ArrayList();
        Map<String, List<BuiltInAtomCustom>> builtInAtomsMap = new HashMap<>();

        for (CustomSWRLAtom swrlAtom : ruleFragment) {
            if (swrlAtom instanceof ClassAtomCustom || swrlAtom instanceof ObjectPropertyAtomCustom || swrlAtom instanceof DataPropertyAtomCustom) {
                standardAtoms.add(swrlAtom);
            } else if (swrlAtom instanceof BuiltInAtomCustom) {
                String firstArg = ((BuiltInAtomCustom) swrlAtom).getArguments()[0];
                if (!builtInAtomsMap.keySet().contains(firstArg)) {
                    //collect all builtin with same first arg
                    List<BuiltInAtomCustom> builtinsWithSameFirstArg = new ArrayList<>();
                    for (CustomSWRLAtom atom : ruleFragment) {
                        if (atom instanceof BuiltInAtomCustom && ((BuiltInAtomCustom) atom).getArguments()[0].equals(firstArg)) {
                            builtinsWithSameFirstArg.add((BuiltInAtomCustom) atom);
                        }
                        builtInAtomsMap.put(firstArg, builtinsWithSameFirstArg);
                    }
                }
            }
        }

        //create AOWLN Elements for standard atoms: Classes, data and object props
        for (CustomSWRLAtom swrlAtom : standardAtoms) {
            if (swrlAtom instanceof ClassAtomCustom) {
                key = swrlAtom.getKey();
                label = swrlAtom.getLabel();
                elementTypeEnum = AOWLNElementTypeEnum.Class;
                aowlnElements.put(key, new AOWLNElement(elementTypeEnum, key, label));
            } else if (swrlAtom instanceof CustomSWRLProperty) {
                CustomSWRLProperty customSWRLProperty = (CustomSWRLProperty) swrlAtom;
                if (swrlAtom instanceof ObjectPropertyAtomCustom) {
                    elementTypeEnum = AOWLNElementTypeEnum.Property;
                    key = swrlAtom.getLabel() + swrlAtom.getKey();
                    label = swrlAtom.getLabel();
                    newElement = new AOWLNElement(elementTypeEnum, key, label);
                    aowlnElements.put(key, newElement);
                    //Input Edge
                    newEdgeElement = new AOWLNEdgeElement(aowlnElements.get(customSWRLProperty.getFirstArgument()), newElement, EdgeTypeEnum.ObjectProperty);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                    //Output Edge
                    newEdgeElement = new AOWLNEdgeElement(newElement, aowlnElements.get(customSWRLProperty.getKey()), EdgeTypeEnum.ObjectProperty);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                }
                if (swrlAtom instanceof DataPropertyAtomCustom) {
                    key = "EL" + swrlAtom.getKey();
                    label = swrlAtom.getLabel();
                    elementTypeEnum = AOWLNElementTypeEnum.Property;
                    newElement = new AOWLNElement(elementTypeEnum, key, label);
                    aowlnElements.put(key, newElement);

                    newEdgeElement = new AOWLNEdgeElement(aowlnElements.get(customSWRLProperty.getFirstArgument()), newElement, EdgeTypeEnum.Normal);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);

                    key = swrlAtom.getKey();
                    label = key;
                    elementTypeEnum = AOWLNElementTypeEnum.Variable;
                    AOWLNElement secondElement = new AOWLNElement(elementTypeEnum, key, label);
                    aowlnElements.put(key, secondElement);

                    newEdgeElement = new AOWLNEdgeElement(newElement, secondElement, EdgeTypeEnum.Normal);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                }
            }
        }

        //create AOWLN Elements for builtin
        for (String firstArg : builtInAtomsMap.keySet()) {
            List<BuiltInAtomCustom> relatedBuiltins = builtInAtomsMap.get(firstArg);
            List<BuiltInAtomCustom> boundBuiltin = new ArrayList<>();

            //there can only be 0 or 1 unbound builtins (e.g. add) but 0 to many bound builtins (lessThan, greaterThan, etc.)
            BuiltInAtomCustom unboundBuiltin = null;
            boolean hasUnboundBuiltin = false;
            for (BuiltInAtomCustom atom : relatedBuiltins) {
                if (!atom.isBound()) {
                    hasUnboundBuiltin = true;
                    unboundBuiltin = atom;
                } else {
                    boundBuiltin.add(atom);
                }
            }

            //variable
            AOWLNElement varElement = new AOWLNElement(AOWLNElementTypeEnum.Variable, firstArg, firstArg);
            aowlnElements.put(firstArg, varElement);

            if (hasUnboundBuiltin) {
                key = firstArg + unboundBuiltin.getLabel();
                label = unboundBuiltin.getLabel();

                //diamond
                elementTypeEnum = AOWLNElementTypeEnum.BuiltInCollection;
                newElement = new AOWLNElement(elementTypeEnum, key, label);
                aowlnElements.put(key, newElement);

                //towards diamond
                for (int k = 1; k < unboundBuiltin.getArguments().length; k++) {
                    String edgeLabel = "";
                    newEdgeElement = new AOWLNEdgeElement(aowlnElements.get(unboundBuiltin.getArguments()[k]), newElement, EdgeTypeEnum.BuiltIn, edgeLabel);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                }

                if (boundBuiltin.size() > 0) {
                    String labelConcat = "";
                    for (BuiltInAtomCustom b : boundBuiltin) {
                        labelConcat = labelConcat + determineBuiltinEdgeLabel(b) + "\n";
                    }
                    newEdgeElement = new AOWLNEdgeElement(newElement, varElement, EdgeTypeEnum.BuiltIn, labelConcat);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                } else {
                    //edge from diamond to variable
                    newEdgeElement = new AOWLNEdgeElement(newElement, varElement, EdgeTypeEnum.Normal);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                }
            } else if (!hasUnboundBuiltin && boundBuiltin.size() > 0) {
                String labelConcat = "";
                for (BuiltInAtomCustom b : boundBuiltin) {
                    labelConcat = labelConcat + determineBuiltinEdgeLabel(b) + "\n";
                }
                AOWLNElement dataProp = null;
                if (aowlnElements.keySet().contains("EL" + boundBuiltin.get(0).getArguments()[0])) {
                    dataProp = aowlnElements.get("EL" + boundBuiltin.get(0).getArguments()[0]);
                    newEdgeElement = new AOWLNEdgeElement(dataProp, varElement, EdgeTypeEnum.BuiltIn, labelConcat);
                    removeRedundantEdge(aowlnElements.get("EL" + firstArg), aowlnElements.get(firstArg), aowlnEdges);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                }
            }
        }

        //clean Linebreak NodeLabels
        for (String elKey : aowlnElements.keySet()) {
            AOWLNElement currentEl = aowlnElements.get(elKey);
            if (currentEl.getLabel().length() > 10 &&
                    (currentEl.getElementType().equals(AOWLNElementTypeEnum.Property) || currentEl.getElementType().equals(AOWLNElementTypeEnum.Class))) {
                currentEl.setLabel(currentEl.getLabel().replaceAll("([a-z]{5,})([A-Z])", "$1\n$2"));
            }
        }

        return createGraphListsForViz(aowlnElements, aowlnEdges);
    }

    private boolean removeRedundantEdge(AOWLNElement from, AOWLNElement to, HashMap<String, AOWLNEdgeElement> aowlnEdges) {

        for (Map.Entry<String, AOWLNEdgeElement> entry : aowlnEdges.entrySet()) {
            String key = entry.getKey();
            AOWLNElement fromElement = entry.getValue().getFrom();
            AOWLNElement toElement = entry.getValue().getTo();
            if (fromElement != null && toElement != null && from.equals(fromElement) && to.equals(toElement)) {
                aowlnEdges.remove(key);
                return true;
            }
        }
        return false;
    }

    public String determineBuiltinEdgeLabel(BuiltInAtomCustom builtInAtomCustom) {
        String edgeLabel = "";
        if (builtInAtomCustom.getLiterals().size() > 0) {
            edgeLabel = builtInAtomCustom.getLabel() + "(";
            for (int index = 0; index < builtInAtomCustom.getLiterals().size(); index++) {
                String arg = builtInAtomCustom.getLiterals().get(index);
                if (index == builtInAtomCustom.getLiterals().size() - 1) {
                    edgeLabel = edgeLabel + arg;
                } else {
                    edgeLabel = edgeLabel + arg + ",";
                }
            }
            edgeLabel = edgeLabel + ")";
        } else {
            if (builtInAtomCustom.getArguments().length == 2) {
                edgeLabel = builtInAtomCustom.getLabel() + "(" + builtInAtomCustom.getArguments()[1] + ")";
            } else {
                edgeLabel = builtInAtomCustom.getLabel();
            }
        }
        return edgeLabel;
    }

    public GraphListsForViz createGraphListsForViz(HashMap<String, AOWLNElement> aowlnElements, HashMap<String, AOWLNEdgeElement> aowlnEdges) {

        NodeInfo[] nodes = new NodeInfo[aowlnElements.size()];
        NodeConnection[] connections = new NodeConnection[aowlnEdges.size()];

        for (int i = 0; i < aowlnElements.values().size(); i++) {
            AOWLNElement element = (AOWLNElement) aowlnElements.values().toArray()[i];
            String caption = element.getLabel();
            String info = element.getIdentifier();
            String type = element.getElementType().toString();
            nodes[i] = new NodeInfo(caption, info, type);
        }

        for (int i = 0; i < aowlnEdges.values().size(); i++) {

            AOWLNEdgeElement element = (AOWLNEdgeElement) aowlnEdges.values().toArray()[i];
            NodeInfo nodeInfoParent = getNodeInfo(nodes, element.getFrom().getIdentifier());
            NodeInfo nodeInfoChild = getNodeInfo(nodes, element.getTo().getIdentifier());
            EdgeTypeEnum edgeTypeEnum = element.getEdgeType();
            String label = element.getLabel();
            connections[i] = new NodeConnection(nodeInfoParent, nodeInfoChild, edgeTypeEnum, label);
        }

        return new GraphListsForViz(nodes, connections);
    }

    public NodeInfo getNodeInfo(NodeInfo[] nodes, String identifier) {
        for (int i = 0; i <= nodes.length; i++) {
            if (nodes[i].getInfo().equals(identifier)) {
                return nodes[i];
            }
        }
        return null;
    }

}
