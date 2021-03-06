package org.jboss.seam.wicket.example.numberguess.test;

import java.util.ServiceLoader;

import javax.inject.Inject;

import junit.framework.AssertionFailedError;

import org.apache.wicket.util.tester.FormTester;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.spi.DeployableContainer;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.seam.solder.reflection.Reflections;
import org.jboss.seam.wicket.SeamApplication;
import org.jboss.seam.wicket.example.numberguess.HomePage;
import org.jboss.seam.wicket.example.numberguess.NumberGuessApplication;
import org.jboss.seam.wicket.example.numberguess.test.util.MavenArtifactResolver;
import org.jboss.seam.wicket.mock.SeamWicketTester;
import org.jboss.seam.wicket.util.NonContextual;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for HomePage and SeamWicketTester.
 * 
 * @author oranheim
 */
public class HomePageTest extends Arquillian
{

   @Deployment
   public static WebArchive createTestArchive()
   {
      WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
         .addPackage(SeamApplication.class.getPackage())
         .addPackage(NonContextual.class.getPackage())
         .addPackage(NumberGuessApplication.class.getPackage())
         .addPackage(SeamWicketTester.class.getPackage())
         .addClasses(Assert.class, AssertionFailedError.class)
         // ugh, arquillian, don't make this so painful :(
         .addResource("org/jboss/seam/wicket/example/numberguess/HomePage.html", "WEB-INF/classes/org/jboss/seam/wicket/example/numberguess/HomePage.html")
         .addWebResource("test-jetty-env.xml", "jetty-env.xml")
         .addWebResource(EmptyAsset.INSTANCE, "beans.xml")
         .addLibraries(
               MavenArtifactResolver.resolve("org.apache.wicket:wicket:1.4.15"))
         .setWebXML("test-web.xml");
      if (!ServiceLoader.load(DeployableContainer.class).iterator().next().getClass().getName().contains("embedded")) {
          war.addLibrary(MavenArtifactResolver.resolve("org.jboss.seam.solder:seam-solder:3.0.0.CR4"));
      }
      return war;
   }
   
   @Inject
   SeamWicketTester tester;
   
   @Test
   public void testGuessNumber() throws Exception
   {
      Assert.assertNotNull(tester);
      
      tester.startPage(HomePage.class);
      
      FormTester form = tester.newFormTester("NumberGuessMain");
      Assert.assertNotNull(form);
      
      form.setValue("inputGuess", "1");
      form.submit("GuessButton");
      
      tester.assertRenderedPage(HomePage.class);
   }
   
   @Test
   public void testRestart() throws Exception
   {
      Assert.assertNotNull(tester);
      
      tester.startPage(HomePage.class);
      
      FormTester form = tester.newFormTester("NumberGuessMain");
      Assert.assertNotNull(form);
      
      form.submit("RestartButton");
      
      tester.assertRenderedPage(HomePage.class);
   }
}
