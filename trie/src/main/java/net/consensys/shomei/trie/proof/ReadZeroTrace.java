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

public class ReadZeroTrace implements Trace {

  private long nextFreeNode;
  public Node<Bytes> subRoot;

  public LeafOpening leftLeaf;

  public LeafOpening rightLeaf;

  public Proof leftProof; // HKEY -
  public Proof rightProof; // HKEY +

  public Bytes key;

  public ReadZeroTrace(
      final long nextFreeNode,
      final Node<Bytes> subRoot,
      final LeafOpening leftLeaf,
      final LeafOpening rightLeaf,
      final Proof leftProof,
      final Proof rightProof,
      final Bytes key) {
    this.nextFreeNode = nextFreeNode;
    this.subRoot = subRoot;
    this.leftLeaf = leftLeaf;
    this.rightLeaf = rightLeaf;
    this.leftProof = leftProof;
    this.rightProof = rightProof;
    this.key = key;
  }

  public ReadZeroTrace(final Node<Bytes> subRoot) {
    this.subRoot = subRoot;
  }

  public long getNextFreeNode() {
    return nextFreeNode;
  }

  public void setNextFreeNode(final long nextFreeNode) {
    this.nextFreeNode = nextFreeNode;
  }

  public Node<Bytes> getSubRoot() {
    return subRoot;
  }

  public void setSubRoot(final Node<Bytes> subRoot) {
    this.subRoot = subRoot;
  }

  public LeafOpening getLeftLeaf() {
    return leftLeaf;
  }

  public void setLeftLeaf(final LeafOpening leftLeaf) {
    this.leftLeaf = leftLeaf;
  }

  public LeafOpening getRightLeaf() {
    return rightLeaf;
  }

  public void setRightLeaf(final LeafOpening rightLeaf) {
    this.rightLeaf = rightLeaf;
  }

  public Proof getLeftProof() {
    return leftProof;
  }

  public void setLeftProof(final Proof leftProof) {
    this.leftProof = leftProof;
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

  public void setKey(final Bytes key) {
    this.key = key;
  }

  @Override
  public int getTransactionType() {
    return READ_ZERO_TRACE_CODE;
  }

  public static ReadZeroTrace readFrom(final RLPInput in) {
    in.enterList();
    final long newNextFreeNode = in.readLongScalar();
    final Node<Bytes> subRoot = new StoredNode<>(null, null, Hash.wrap(in.readBytes32()));
    final LeafOpening leftLeaf = LeafOpening.readFrom(in.readBytes());
    final LeafOpening rightLeaf = LeafOpening.readFrom(in.readBytes());
    final Proof leftProof = Proof.readFrom(in);
    final Proof rightProof = Proof.readFrom(in);
    final Bytes key = in.readBytes();
    in.leaveList();
    return new ReadZeroTrace(
        newNextFreeNode, subRoot, leftLeaf, rightLeaf, leftProof, rightProof, key);
  }

  @Override
  public void writeTo(final RLPOutput out) {
    out.startList();
    out.writeLongScalar(nextFreeNode);
    out.writeBytes(subRoot.getHash());
    out.writeBytes(leftLeaf.getEncodesBytes());
    out.writeBytes(rightLeaf.getEncodesBytes());
    leftProof.writeTo(out);
    rightProof.writeTo(out);
    out.writeBytes(key);
    out.endList();
  }
}