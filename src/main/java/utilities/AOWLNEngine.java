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
        int beginBuiltInInRuleFragmentList = -1;
        List<CustomSWRLAtom> done = new ArrayList<>();
        Multimap<BuiltInAtomCustom, BuiltInAtomCustom> builtInCollectionsMMap = HashMultimap.create();

        String key = null;
        String label = null;
        AOWLNElementTypeEnum elementTypeEnum = null;
        AOWLNElement newElement = null;
        AOWLNEdgeElement newEdgeElement = null;
        for (int i = 0; i < ruleFragment.size(); i++) {
            CustomSWRLAtom swrlAtom = ruleFragment.get(i);
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
            } else if (swrlAtom instanceof BuiltInAtomCustom) {
                if (beginBuiltInInRuleFragmentList == -1) {
                    beginBuiltInInRuleFragmentList = i;
                }
                //Check if already exists, then create a BuiltInCollection
                //elementTypeEnum = AOWLNElementTypeEnum.BuiltInCollection;
                BuiltInAtomCustom builtInAtomCustom = (BuiltInAtomCustom) swrlAtom;
                key = builtInAtomCustom.getArguments()[0];
                boolean isBuiltInCollection = false;

                for (int j = beginBuiltInInRuleFragmentList++; j < ruleFragment.size(); j++) {
                    if (!ruleFragment.get(j).equals(builtInAtomCustom) && !done.contains(ruleFragment.get(j))) {
                        String firstArgumentOfComparingBuiltIn = ((BuiltInAtomCustom) ruleFragment.get(j)).getArguments()[0];
                        if (key.equals(firstArgumentOfComparingBuiltIn)) {
                            isBuiltInCollection = true;
                            done.add(ruleFragment.get(j));
                            builtInCollectionsMMap.put(builtInAtomCustom, (BuiltInAtomCustom) ruleFragment.get(j));
                        }
                    }
                }
                if (isBuiltInCollection) {
                    key = "BC" + builtInAtomCustom.getKey() + new UID().toString();
                    label = "BC";
                    elementTypeEnum = AOWLNElementTypeEnum.BuiltInCollection;
                    newElement = new AOWLNElement(elementTypeEnum, key, label);
                    aowlnElements.put(key, newElement);
                    newEdgeElement = new AOWLNEdgeElement(aowlnElements.get("EL" + builtInAtomCustom.getArguments()[0]), newElement, EdgeTypeEnum.Normal);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                    newEdgeElement = new AOWLNEdgeElement(newElement, aowlnElements.get(builtInAtomCustom.getArguments()[0]), EdgeTypeEnum.Normal);
                    aowlnEdges.put(new UID().toString(), newEdgeElement);
                    removeRedundantEdge(aowlnElements.get("EL" + builtInAtomCustom.getArguments()[0]), aowlnElements.get(builtInAtomCustom.getArguments()[0]), aowlnEdges);
                }

                if (!isBuiltInCollection && !done.contains(swrlAtom)) {
                    if (builtInAtomCustom.isBound()) {
                        AOWLNElement variableElement = aowlnElements.get(builtInAtomCustom.getArguments()[0]);
                        removeRedundantEdge(aowlnElements.get("EL" + builtInAtomCustom.getArguments()[0]), variableElement, aowlnEdges);
                        String edgeLabel = "";

                        if (builtInAtomCustom.getLiterals().size() > 0) {
                            edgeLabel = builtInAtomCustom.getLabel() + "(";
                            //add builtin arguments to label
                            /*for(int index =0; index< builtInAtomCustom.getArguments().length; index++){
                                String arg = builtInAtomCustom.getArguments()[index];
                                if(index == builtInAtomCustom.getArguments().length-1){
                                    edgeLabel = edgeLabel+ arg;
                                }else {
                                    edgeLabel = edgeLabel+ arg+",";
                                }
                            }*/
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
                            edgeLabel = builtInAtomCustom.getLabel();
                        }

                        newEdgeElement = new AOWLNEdgeElement(aowlnElements.get("EL" + builtInAtomCustom.getArguments()[0]), variableElement, EdgeTypeEnum.BuiltIn, edgeLabel);
                        aowlnEdges.put(new UID().toString(), newEdgeElement);
                        //  removeRedundantEdge(aowlnElements.get("EL" + builtInAtomCustom.getArguments()[0]), newElement, aowlnEdges);
                    } else {
                        newElement = new AOWLNElement(AOWLNElementTypeEnum.Variable, builtInAtomCustom.getArguments()[0], builtInAtomCustom.getArguments()[0]);
                        aowlnElements.put(builtInAtomCustom.getArguments()[0], newElement);
                        for (int k = 1; k < builtInAtomCustom.getArguments().length; k++) {
                            String edgeLabel = "";
                            edgeLabel = builtInAtomCustom.getLabel();

                            newEdgeElement = new AOWLNEdgeElement(aowlnElements.get(builtInAtomCustom.getArguments()[k]), newElement, EdgeTypeEnum.BuiltIn, edgeLabel);
                            aowlnEdges.put(new UID().toString(), newEdgeElement);
                        }
                    }
                }
            }
        }

        //clean Linebreak NodeLabels
        for (String elKey : aowlnElements.keySet()) {
            AOWLNElement currentEl = aowlnElements.get(elKey);
            if (currentEl.getLabel().length() > 10 &&
                    (currentEl.getElementType().equals(AOWLNElementTypeEnum.Property) || currentEl.getElementType().equals(AOWLNElementTypeEnum.Class))) {
                currentEl.setLabel(currentEl.getLabel().replaceAll("(.)([A-Z])", "$1\n$2"));
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
