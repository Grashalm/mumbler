package mumbler.graal;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import mumbler.graal.fromsimple.node.MumblerListNode;
import mumbler.graal.node.MumblerNode;
import mumbler.graal.node.MumblerRootNode;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GraalMumblerMain {
    public static void main(String[] args) throws IOException {
        assert args.length < 2 : "Mumbler only accepts 1 or 0 files";
        if (args.length == 0) {
            startREPL();
        } else {
            runMumbler(args[0]);
        }
    }

    private static void startREPL() throws IOException {
        FrameDescriptor frameDescriptor = new FrameDescriptor();
        VirtualFrame virtualFrame = Truffle.getRuntime().createVirtualFrame(
                new Object[] {}, frameDescriptor);

        Console console = System.console();
        while (true) {
            // READ
            String data = console.readLine("~> ");
            if (data == null) {
                // EOF sent
                break;
            }
            List<MumblerNode> nodes =
                    Reader.read(new ByteArrayInputStream(data.getBytes()));

            // EVAL
            Object result = MumblerListNode.EMPTY;
            for (MumblerNode node : nodes) {
                result = node.execute(virtualFrame);
            }

            // PRINT
            if (result != MumblerListNode.EMPTY) {
                System.out.println(result);
            }
        }
    }

    private static void runMumbler(String filename) throws IOException {
        List<MumblerNode> nodes = Reader.read(new FileInputStream(filename));
        FrameDescriptor frameDescriptor = Reader.frameDesriptors.pop();
        MumblerRootNode root = MumblerRootNode.lambda(new FrameSlot[] {},
                nodes.toArray(new MumblerNode[] {}), frameDescriptor);
        root.getCallTarget().call();
    }
}
