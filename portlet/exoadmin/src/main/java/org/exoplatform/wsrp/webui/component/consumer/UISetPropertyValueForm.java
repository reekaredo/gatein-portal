/*
 * JBoss, a division of Red Hat
 * Copyright 2010, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.wsrp.webui.component.consumer;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.wsrp.webui.component.UIMappedForm;
import org.gatein.wsrp.consumer.RegistrationProperty;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
@ComponentConfig(
   lifecycle = UIFormLifecycle.class,
   template = "system:/groovy/webui/form/UIForm.gtmpl",
   events = {
      @EventConfig(listeners = UISetPropertyValueForm.SaveActionListener.class),
      @EventConfig(listeners = UISetPropertyValueForm.CancelActionListener.class)
   })
public class UISetPropertyValueForm extends UIMappedForm
{
   private UIFormStringInput value;
   private static final String[] ACTIONS = new String[]{"Save", "Cancel"};
   private RegistrationProperty prop;

   public UISetPropertyValueForm()
   {
      value = new UIFormStringInput("value", null);
      setActions(ACTIONS);
   }

   public void setProperty(RegistrationProperty prop)
   {
      this.prop = prop;
      try
      {
         setBackingBean(prop);
      }
      catch (Exception e)
      {
         e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
   }

   static public class CancelActionListener extends EventListener<UISetPropertyValueForm>
   {
      @Override
      public void execute(Event<UISetPropertyValueForm> event) throws Exception
      {
         // simply close the popup
         UIPopupWindow popup = event.getSource().getParent();
         popup.setRendered(false);
         popup.setShow(false);
      }
   }

   static public class SaveActionListener extends EventListener<UISetPropertyValueForm>
   {
      @Override
      public void execute(Event<UISetPropertyValueForm> event) throws Exception
      {

      }
   }
}
