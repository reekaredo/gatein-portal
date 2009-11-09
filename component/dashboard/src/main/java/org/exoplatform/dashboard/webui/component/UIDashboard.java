/**
 * Copyright (C) 2009 eXo Platform SAS.
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

package org.exoplatform.dashboard.webui.component;

import org.exoplatform.portal.webui.application.UIGadget;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

@ComponentConfigs({@ComponentConfig(template = "classpath:groovy/dashboard/webui/component/UIDashboard.gtmpl", events = {
   @EventConfig(listeners = UIDashboardContainer.MoveGadgetActionListener.class),
   @EventConfig(listeners = UIDashboardContainer.AddNewGadgetActionListener.class),
   @EventConfig(listeners = UIDashboard.SetShowSelectContainerActionListener.class),
   @EventConfig(listeners = UIDashboardContainer.DeleteGadgetActionListener.class),
   @EventConfig(listeners = UIDashboard.MinimizeGadgetActionListener.class),
   @EventConfig(listeners = UIDashboard.MaximizeGadgetActionListener.class)})})
public class UIDashboard extends UIContainer
{

   public static String GADGET_POPUP_ID = "UIAddGadgetPopup";

   private boolean isShowSelectPopup = false;

   private String aggregatorId;

   private UIGadget maximizedGadget;

   public UIDashboard() throws Exception
   {
      UIPopupWindow popup = addChild(UIPopupWindow.class, null, GADGET_POPUP_ID);
      popup.setUIComponent(createUIComponent(UIDashboardSelectContainer.class, null, null));
      addChild(UIDashboardContainer.class, null, null).setColumns(3);
   }

   public void setColumns(int num) throws Exception
   {
      getChild(UIDashboardContainer.class).setColumns(num);
   }

   public void setContainerTemplate(String template)
   {
      getChild(UIDashboardContainer.class).setContainerTemplate(template);
   }

   public boolean canEdit()
   {
      DashboardParent parent = (DashboardParent)getParent();
      return parent.canEdit();
   }

   public boolean isShowSelectPopup()
   {
      return isShowSelectPopup;
   }

   public void setShowSelectPopup(final boolean value)
   {
      this.isShowSelectPopup = value;
      ((UIPopupWindow)getChildById(GADGET_POPUP_ID)).setShow(value);
   }

   public String getAggregatorId()
   {
      return aggregatorId;
   }

   public void setAggregatorId(String aggregatorId)
   {
      this.aggregatorId = aggregatorId;
   }

   public UIGadget getMaximizedGadget()
   {
      return maximizedGadget;
   }

   public void setMaximizedGadget(UIGadget gadget)
   {
      maximizedGadget = gadget;
   }

   public static class SetShowSelectContainerActionListener extends EventListener<UIDashboard>
   {
      public final void execute(final Event<UIDashboard> event) throws Exception
      {
         UIDashboard uiDashboard = (UIDashboard)event.getSource();
         if (!uiDashboard.canEdit())
         {
            return;
         }
         PortletRequestContext pcontext = (PortletRequestContext)event.getRequestContext();
         boolean isShow = Boolean.parseBoolean(pcontext.getRequestParameter("isShow"));
         uiDashboard.setShowSelectPopup(isShow);
         String windowId = uiDashboard.getChild(UIDashboardContainer.class).getWindowId();
         event.getRequestContext().addUIComponentToUpdateByAjax(uiDashboard.getChildById(GADGET_POPUP_ID));
         if (isShow)
         {
            event.getRequestContext().getJavascriptManager().addCustomizedOnLoadScript(
               "eXo.webui.UIDashboard.onLoad('" + windowId + "'," + uiDashboard.canEdit() + ");");
         }
      }
   }

   public static class MinimizeGadgetActionListener extends EventListener<UIDashboard>
   {
      public final void execute(final Event<UIDashboard> event) throws Exception
      {
         WebuiRequestContext context = event.getRequestContext();
         UIDashboard uiDashboard = event.getSource();
         String objectId = context.getRequestParameter(OBJECTID);
         String minimized = context.getRequestParameter("minimized");

         UIGadget uiGadget = uiDashboard.getChild(UIDashboardContainer.class).getUIGadget(objectId);
         uiGadget.getProperties().setProperty("minimized", minimized);
         uiDashboard.getChild(UIDashboardContainer.class).save();
         context.addUIComponentToUpdateByAjax(uiGadget);
      }
   }

   public static class MaximizeGadgetActionListener extends EventListener<UIDashboard>
   {
      public final void execute(final Event<UIDashboard> event) throws Exception
      {
         WebuiRequestContext context = event.getRequestContext();
         UIDashboard uiDashboard = event.getSource();
         String objectId = context.getRequestParameter(OBJECTID);
         String maximize = context.getRequestParameter("maximize");
         UIDashboardContainer uiDashboardCont = uiDashboard.getChild(UIDashboardContainer.class);
         UIGadget uiGadget = uiDashboardCont.getUIGadget(objectId);
         if (maximize.equals("maximize"))
         {
            uiGadget.setView(UIGadget.CANVAS_VIEW);
            uiDashboard.setMaximizedGadget(uiGadget);
         }
         else
         {
            uiGadget.setView(UIGadget.HOME_VIEW);
            uiDashboard.setMaximizedGadget(null);
         }
      }
   }
}
