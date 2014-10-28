package mumbler.graal;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.SymbolNode;
import mumbler.graal.node.SymbolNodeFactory;
import mumbler.graal.node.invoke.InvocationNode;
import mumbler.graal.node.literal.BooleanNode;
import mumbler.graal.node.literal.LiteralListNode;
import mumbler.graal.node.literal.NumberNode;
import mumbler.graal.node.special.DefineNodeFactory;
import mumbler.graal.type.MumblerNull;

import com.oracle.truffle.api.frame.FrameDescriptor;


public class Reader {
    public static final Stack<FrameDescriptor> frameDesriptors = new Stack<>();

    static {
        frameDesriptors.push(new FrameDescriptor());
    }

    public static List<MumblerNode> read(InputStream istream) throws IOException {
        return read(new PushbackReader(new InputStreamReader(istream)));
    }

    private static List<MumblerNode> read(PushbackReader pstream)
            throws IOException {
        List<MumblerNode> nodes = new ArrayList<>();

        readWhitespace(pstream);
        char c = (char) pstream.read();
        while ((byte) c != -1) {
            pstream.unread(c);
            nodes.add(readNode(pstream));
            readWhitespace(pstream);
            c = (char) pstream.read();
        }

        assert frameDesriptors.size() == 1;
        return nodes;
    }

    public static MumblerNode readNode(PushbackReader pstream)
            throws IOException {
        char c = (char) pstream.read();
        pstream.unread(c);
        if (c == '(') {
            return readList(pstream);
        } else if (Character.isDigit(c)) {
            return readNumber(pstream);
        } else if (c == '#') {
            return readBoolean(pstream);
        } else if (c == ')') {
            throw new IllegalArgumentException("Unmatched close paren");
        } else {
            return readSymbol(pstream);
        }
    }

    private static void readWhitespace(PushbackReader pstream)
            throws IOException {
        char c = (char) pstream.read();
        while (Character.isWhitespace(c)) {
            c = (char) pstream.read();
        }
        pstream.unread(c);
    }

    private static SymbolNode readSymbol(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (!(Character.isWhitespace(c) || (byte) c == -1 || c == '(' || c == ')')) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        return SymbolNodeFactory.create(
                frameDesriptors.peek().findOrAddFrameSlot(b.toString()));
    }

    private static MumblerNode readList(PushbackReader pstream)
            throws IOException {
        char paren = (char) pstream.read();
        assert paren == '(' : "Reading a list must start with '('";
        List<MumblerNode> list = new ArrayList<>();

        MumblerNode element = readListElement(pstream);
        if (element == null) {
            return new LiteralListNode(MumblerNull.EMPTY);
        } else if (element instanceof SymbolNode &&
                isSpecialForm((SymbolNode) element)) {
            return readSpecialForm((SymbolNode) element, pstream);
        } else {
            do {
                list.add(element);
                element = readListElement(pstream);
            } while (element != null);
        }
        return new InvocationNode(list.get(0),
                list.subList(1, list.size() - 1).toArray(new MumblerNode[] {}));
    }

    private static boolean isSpecialForm(SymbolNode symbol) {
        switch((String) symbol.getSlot().getIdentifier()) {
        case "define":
        case "if":
        case "lambda":
        case "quote":
            return true;
        default:
            return false;
        }
    }

    private static MumblerNode readSpecialForm(SymbolNode name,
            PushbackReader pstream) throws IOException {
        String id = (String) name.getSlot().getIdentifier();
        if (id.equals("define")) {
            SymbolNode nameNode = (SymbolNode) readListElement(pstream);
            MumblerNode valueNode = readListElement(pstream);
            MumblerNode end = readListElement(pstream);
            assert end == null;
            return DefineNodeFactory.create(valueNode, nameNode.getSlot());
        } else if (id.equals("if")) {
            // TODO: finish
            return null;
        } else if (id.equals("lambda")) {
            // TODO: finish
            return null;
        } else if (id.equals("quote")) {
            // TODO: finish
            return null;
        } else {
            throw new IllegalArgumentException("Unknown special form: " + id);
        }
    }

    private static MumblerNode readListElement(PushbackReader pstream)
            throws IOException {
        readWhitespace(pstream);
        char c = (char) pstream.read();

        if (c == ')') {
            // end of list
            return null;
        } else if ((byte) c == -1) {
            throw new EOFException("EOF reached before closing of list");
        } else {
            pstream.unread(c);
            return readNode(pstream);
        }
    }

    private static NumberNode readNumber(PushbackReader pstream)
            throws IOException {
        StringBuilder b = new StringBuilder();
        char c = (char) pstream.read();
        while (Character.isDigit(c)) {
            b.append(c);
            c = (char) pstream.read();
        }
        pstream.unread(c);
        return new NumberNode(Long.valueOf(b.toString(), 10));
    }

    private static BooleanNode readBoolean(PushbackReader pstream)
            throws IOException {
        char hash = (char) pstream.read();
        assert hash == '#' : "Reading a boolean must start with '#'";

        Object name = readSymbol(pstream).getSlot().getIdentifier();
        if ("t".equals(name)) {
            return BooleanNode.TRUE;
        } else if ("f".equals(name)) {
            return BooleanNode.FALSE;
        } else {
            throw new IllegalArgumentException("Unknown value: #" + name);
        }
    }
}
