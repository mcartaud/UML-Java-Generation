package org.obeonetwork.pim.uml2.gen.java.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class ImportServices {
	private static final String IMPORT = "import ";
	private static final String END_IMPORT = ";" + System.getProperty("line.separator");
	private static final List<String> JAVA_LANG_TYPES = Arrays.asList(new String[]{"Boolean",
					"boolean", "Integer", "int", "String", "char", "Char", "long", "Long", "double",
					"Double", "Float", "float", "Byte", "byte"});
	private static final List<String> JAVA_UTIL_TYPES = Arrays.asList(new String[]{"Date"});
	
	public String reqImport(Classifier aClassifier) {
		Set<String> importedTypes = new LinkedHashSet<String>();
		
		List<Property> attributes = aClassifier.getAttributes();
		for (Property property : attributes) {
			String qualifiedName = this.qualifiedName(property.getType());
			if (qualifiedName != null) {					
				importedTypes.add(qualifiedName);
			}
			if (property.getUpper() != 1) {
				String collectionQualifiedName = this.collectionQualifiedName(property.isOrdered(), property.isUnique());
				if (collectionQualifiedName != null) {
					importedTypes.add(collectionQualifiedName);
				}
			}
		}
		
		List<Operation> operations = aClassifier.getOperations();
		for (Operation operation : operations) {
			String qualifiedName = this.qualifiedName(operation.getType());
			if (qualifiedName != null) {					
				importedTypes.add(qualifiedName);
			}
			if (operation.getUpper() != 1) {
				String collectionQualifiedName = this.collectionQualifiedName(operation.isOrdered(), operation.isUnique());
				if (collectionQualifiedName != null) {
					importedTypes.add(collectionQualifiedName);
				}
			}
			
			List<Parameter> ownedParameters = operation.getOwnedParameters();
			for (Parameter parameter : ownedParameters) {
				qualifiedName = this.qualifiedName(parameter.getType());
				if (qualifiedName != null) {					
					importedTypes.add(qualifiedName);
				}
				if (parameter.getUpper() != 1) {
					String collectionQualifiedName = this.collectionQualifiedName(parameter.isOrdered(), parameter.isUnique());
					if (collectionQualifiedName != null) {
						importedTypes.add(collectionQualifiedName);
					}
				}
			}
		}

		List<String> sortedImportedTypes = new ArrayList<String>(importedTypes);
		Collections.sort(sortedImportedTypes);
		
		StringBuilder stringBuilder = new StringBuilder();
		for (String importedType : sortedImportedTypes) {			
			stringBuilder.append(IMPORT + importedType + END_IMPORT);
		}
		
		return stringBuilder.toString();
	}
	
	private String collectionQualifiedName(boolean ordered, boolean unique) {
		String result = "java.util.ArrayList";
		if (ordered && unique) {
			result = "java.util.LinkedHashSet";
		} else if (!ordered && !unique) {
			result = "java.util.ArrayList";
		} else if (!ordered && unique) {
			result = "java.util.HashSet";
		}
		return result;
	}

	private String qualifiedName(Type type) {
		String result = null;
		if (type != null) {
			result = type.getQualifiedName();
			if (JAVA_LANG_TYPES.contains(type.getName())) {
				result = null;
			} else if (JAVA_UTIL_TYPES.contains(type.getName())) {
				result = "java.util." + type.getName();
			}
		}
		return result;
	}
}