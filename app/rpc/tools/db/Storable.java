package rpc.tools.db;

import rpc.shared.data.Serializable;

public interface Storable extends Serializable {
    String store();
}
