package com.shengsiyuan.thrift;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import com.shengsiyuan.thrift.generated.PersonService;

public class ThriftServer {

    public static void main(String[] arg) throws Exception {
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args args = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        args.protocolFactory(new TCompactProtocol.Factory());
        args.transportFactory(new TFramedTransport.Factory());
        args.processorFactory(new TProcessorFactory(processor));

        TServer server = new THsHaServer(args);

        System.out.println("Thrift Server Started!");

        server.serve();
    }
}
