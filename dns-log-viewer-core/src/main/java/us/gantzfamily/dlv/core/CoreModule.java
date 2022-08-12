package us.gantzfamily.dlv.core;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import us.gantzfamily.dlv.core.impl.DnsLogReaderImpl;

public class CoreModule extends AbstractModule {
    
    @Override
    protected void configure() {
        bind(DnsLogReader.class).to(DnsLogReaderImpl.class).in(Singleton.class);
    }
}
