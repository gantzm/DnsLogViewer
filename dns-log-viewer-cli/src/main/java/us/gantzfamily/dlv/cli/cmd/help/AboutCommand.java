package us.gantzfamily.dlv.cli.cmd.help;

import us.gantzfamily.dlv.cli.Main;
import us.gantzfamily.dlv.cli.cmd.AbstractCommand;
import us.gantzfamily.dlv.common.JavaPackageInfo;

public class AboutCommand extends AbstractCommand<AboutParameters> {

    public AboutCommand() {
        super("about", new AboutParameters());
    }

    @Override
    public int run() {

        final JavaPackageInfo pi = new JavaPackageInfo(Main.class);

        con.info("");
        con.info("");
        con.info("Title .....: %s", pi.getTitle());
        con.info("Version ...: %s", pi.getVersion());
        con.info("Vendor ....: %s", pi.getVendor());
        con.info("");
        con.info("");

        return 0;
    }

}
