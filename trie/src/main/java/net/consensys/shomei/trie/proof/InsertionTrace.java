/*
 * Copyright ConsenSys Software Inc., 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.consensys.shomei.trie.proof;

import net.consensys.shomei.trie.model.LeafOpening;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.ethereum.rlp.RLPInput;
import org.hyperledger.besu.ethereum.rlp.RLPOutput;
import org.hyperledger.besu.ethereum.trie.Node;
import org.hyperledger.besu.ethereum.trie.StoredNode;

public class InsertionTrace implements Trace {

  private long newNextFreeNode;
  public Node<Bytes> oldSubRoot;
  public Node<Bytes> newSubRoot;

  // `New` correspond to the inserted leaf
  public Proof leftProof; // HKEY -
  public Proof newProof; // hash(k)
  public Proof rightProof; // HKEY +
  public Bytes key;
  public Bytes value;

  // Value of the leaf opening before being modified
  public LeafOpening priorLeftLeaf;
  public LeafOpening priorRightLeaf;

  public InsertionTrace(
      final long newNextFreeNode,
      final Node<Bytes> oldSubRoot,
      final Node<Bytes> newSubRoot,
      final Proof leftProof,
      final Proof newProof,
      final Proof rightProof,
      final Bytes key,
      final Bytes value,
      final LeafOpening priorLeftLeaf,
      final LeafOpening priorRightLeaf) {
    this.newNextFreeNode = newNextFreeNode;
    this.oldSubRoot = oldSubRoot;
    this.newSubRoot = newSubRoot;
    this.leftProof = leftProof;
    this.newProof = newProof;
    this.rightProof = rightProof;
    this.key = key;
    this.value = value;
    this.priorLeftLeaf = priorLeftLeaf;
    this.priorRightLeaf = priorRightLeaf;
  }

  public InsertionTrace(final Node<Bytes> oldSubRoot) {
    this.oldSubRoot = oldSubRoot;
  }

  public void setKey(final Bytes key) {
    this.key = key;
  }

  public void setValue(final Bytes value) {
    this.value = value;
  }

  public long getNewNextFreeNode() {
    return newNextFreeNode;
  }

  public void setNewNextFreeNode(final long newNextFreeNode) {
    this.newNextFreeNode = newNextFreeNode;
  }

  public Node<Bytes> getOldSubRoot() {
    return oldSubRoot;
  }

  public Node<Bytes> getNewSubRoot() {
    return newSubRoot;
  }

  public void setNewSubRoot(final Node<Bytes> newSubRoot) {
    this.newSubRoot = newSubRoot;
  }

  public Proof getLeftProof() {
    return leftProof;
  }

  public void setLeftProof(final Proof leftProof) {
    this.leftProof = leftProof;
  }

  public Proof getNewProof() {
    return newProof;
  }

  public void setNewProof(final Proof newProof) {
    this.newProof = newProof;
  }

  public Proof getRightProof() {
    return rightProof;
  }

  public void setRightProof(final Proof rightProof) {
    this.rightProof = rightProof;
  }

  public Bytes getKey() {
    return key;
  }

  public Bytes getValue() {
    return value;
  }

  public LeafOpening getPriorLeftLeaf() {
    return priorLeftLeaf;
  }

  public LeafOpening getPriorRightLeaf() {
    return priorRightLeaf;
  }

  public void setPriorLeftLeaf(final LeafOpening priorLeftLeaf) {
    this.priorLeftLeaf = priorLeftLeaf;
  }

  public void setPriorRightLeaf(final LeafOpening priorRightLeaf) {
    this.priorRightLeaf = priorRightLeaf;
  }

  @Override
  public int getTransactionType() {
    return INSERTION_TRACE_CODE;
  }

  public static InsertionTrace readFrom(final RLPInput in) {
    in.enterList();
    final long newNextFreeNode = in.readLongScalar();
    final Node<Bytes> oldSubRoot = new StoredNode<>(null, null, Hash.wrap(in.readBytes32()));
    final Node<Bytes> newSubRoot = new StoredNode<>(null, null, Hash.wrap(in.readBytes32()));
    final Proof leftProof = Proof.readFrom(in);
    final Proof newProof = Proof.readFrom(in);
    final Proof rightProof = Proof.readFrom(in);
    final Bytes key = in.readBytes();
    final Bytes value = in.readBytes();
    final LeafOpening priorLeftLeaf = LeafOpening.readFrom(in.readBytes());
    final LeafOpening priorRightLeaf = LeafOpening.readFrom(in.readBytes());
    in.leaveList();
    return new InsertionTrace(
        newNextFreeNode,
        oldSubRoot,
        newSubRoot,
        leftProof,
        newProof,
        rightProof,
        key,
        value,
        priorLeftLeaf,
        priorRightLeaf);
  }

  @Override
  public void writeTo(final RLPOutput out) {
    out.startList();
    out.writeLongScalar(newNextFreeNode);
    out.writeBytes(oldSubRoot.getHash());
    out.writeBytes(newSubRoot.getHash());
    leftProof.writeTo(out);
    newProof.writeTo(out);
    rightProof.writeTo(out);
    out.writeBytes(key);
    out.writeBytes(value);
    out.writeBytes(priorLeftLeaf.getEncodesBytes());
    out.writeBytes(priorRightLeaf.getEncodesBytes());
    out.endList();
  }
}