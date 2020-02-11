package ir.cfg;

import ir.cfg.*;
import ir.IRUtil;
import ir.IRFunction;
import ir.IRInstruction;
// import ir.operand.IROperand;
// import ir.operand.IRLabelOperand;
// import ir.operand.IRVariableOperand;

// import ir.IRPrinter;

import java.util.Set;
// import java.util.HashSet;
import java.util.TreeSet;
import java.util.LinkedHashSet;
// import java.util.Map;
// import java.util.HashMap;
// import java.util.List;
// import java.util.ArrayList;
// import java.util.Arrays;

public class DominatorTree {

	public class TreeNode implements Comparable<TreeNode> {
		public BasicBlockBase block;
		public BasicBlockBase parent;
		public Set<BasicBlockBase> children;

		public TreeNode(BasicBlockBase block, BasicBlockBase parent, Set<BasicBlockBase> children) {
			this.block = block;
			this.parent = parent;
			this.children = children;
		}

		public TreeNode(BasicBlockBase block, BasicBlockBase parent) {
			this(block, parent, new LinkedHashSet<BasicBlockBase>());
		}

		public TreeNode(BasicBlockBase block) {
			this(block, null, new LinkedHashSet<BasicBlockBase>());
		}

		public Set<BasicBlockBase> getChildren() {
			return this.children;
		}

		public BasicBlockBase getParent() {
			return this.parent;
		}

		@Override
		public int compareTo(TreeNode other) {
			return this.block.blocknum - other.block.blocknum;
		}

		@Override
		public boolean equals(Object other) {
			return this.block.equals(((TreeNode)other).block);
		}
	}

	// BasicBlockBase root;
	// ... (like height? or # of nodes in tree?)

	public TreeNode root;
	public Set<TreeNode> treeNodes;

	public DominatorTree(BasicBlockBase entryNode) {
		this.root = new TreeNode(entryNode);
		this.treeNodes = new TreeSet<TreeNode>();
		this.treeNodes.add(this.root);
	}


	public TreeNode add(BasicBlockBase newNode, BasicBlockBase parent, Set<BasicBlockBase> children) {
		if (parent == null || !nodeExistsForBlock(parent))
			return null;
		TreeNode child = new TreeNode(newNode, parent, children);
		getNodeForBlock(parent).getChildren().add(newNode);
		this.treeNodes.add(child);
		return child;
	}

	public TreeNode add(BasicBlockBase newNode, BasicBlockBase parent) {
		return add(newNode, parent, new LinkedHashSet<BasicBlockBase>());
	}

	public TreeNode add(BasicBlockBase newNode) {
		if (nodeExistsForBlock(newNode)) {
			return add(newNode, getParentOfNode(newNode));
		}
		// Will assume the block should be added as a child of the root node
		// return add(newNode, this.root);
		return add(newNode, newNode.iDom);
	}


	public boolean addChildTo(TreeNode node, TreeNode child) {
		if (!this.treeNodes.contains(child)) 
			this.treeNodes.add(child);
		child.parent = node.block;
		node.getChildren().add(child.block);
		return true;
	}

	public boolean addChildTo(BasicBlockBase node, BasicBlockBase child) {
		if (!nodeExistsForBlock(child))
			return addChildTo(getNodeForBlock(node), new TreeNode(child));
		return addChildTo(getNodeForBlock(node), getNodeForBlock(child));
	}

	public Set<BasicBlockBase> getChildrenOfRoot() {
		return this.root.children;
	}

	public Set<BasicBlockBase> getChildrenOfNode(BasicBlockBase block) {
		for (TreeNode node : this.treeNodes) {
			if (node.block.equals(block))
				return node.children;
		}
		return null;
	}

	public BasicBlockBase getParentOfNode(BasicBlockBase block) {
		for (TreeNode node : this.treeNodes) {
			if (node.block.equals(block))
				return node.parent;
		}
		return null;
	}

	public boolean nodeExistsForBlock(BasicBlockBase block) {
		for (TreeNode node : this.treeNodes) {
			if (node.block.equals(block))
				return true;
		}
		return false;
	}

	public TreeNode getNodeForBlock(BasicBlockBase block) {
		for (TreeNode node : this.treeNodes) {
			if (node.block.equals(block))
				return node;
		}
		return null;
	}


	public TreeNode getRootNode() { return this.root; }

	public Set<TreeNode> getTreeNodes() { return this.treeNodes; }

	public void printTree() {
		for (TreeNode node : this.treeNodes) {
			System.out.println("[DominatorTree] NODE "+node.block.blocknum+":");
			System.out.println("\tparent = "+ (node.parent != null ? node.parent.blocknum : "NULL"));
			System.out.print("\tchildren = { ");
			for (BasicBlockBase child : node.children) {
				System.out.print(child.blocknum + ", ");
			}
			System.out.println("}\n");
		}
	}
}
