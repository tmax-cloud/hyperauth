<#import "template.ftl" as layout>
<@layout.mainLayout active='sessions' bodyClass='sessions'; section>

    <@layout.contentHeader ; section>
        ${msg("sessionsHtmlTitle")}
    </@layout.contentHeader>


    <form action="${url.sessionsUrl}" method="post">
        <input type="hidden" id="stateChecker" name="stateChecker" value="${stateChecker}">
        <button id="logout-all-sessions" class="${properties.kcButtonClass!} ${properties.kcButtonDefaultClass!}">${msg("doLogOutAllSessions")}</button>
    </form>

    <table class="table">
        <thead>
        <tr>
            <th>${msg("services")}</th>
            <th>${msg("ip")}</th>
            <th>${msg("started")}</th>
            <th>${msg("lastAccess")}</th>
            <th>${msg("expires")}</th>            
        </tr>
        </thead>

        <tbody>
        <#list sessions.sessions as session>
            <#list session.clients as client>
                <tr>
                    <td>${client}</td>
                    <td>${session.ipAddress}</td>
                    <td>${session.started?datetime}</td>
                    <td>${session.lastAccess?datetime}</td>
                    <td>${session.expires?datetime}</td>                
                </tr>
            </#list>
        </#list>
        </tbody>

    </table>

    

</@layout.mainLayout>
